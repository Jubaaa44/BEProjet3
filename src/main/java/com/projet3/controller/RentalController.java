package com.projet3.controller;

import com.projet3.dto.RentalDTO;
import com.projet3.entity.RentalEntity;
import com.projet3.entity.UserEntity;
import com.projet3.mapper.RentalMapper;
import com.projet3.response.RentalsResponse;
import com.projet3.service.RentalService;
import com.projet3.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Rental Controller", description = "Gestion des locations")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(RentalController.class);

    /**
     * Récupérer toutes les locations.
     * @return ResponseEntity contenant la liste des locations.
     */
    @Operation(summary = "Obtenir toutes les locations", description = "Retourne la liste de toutes les locations.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Locations récupérées avec succès"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping
    public ResponseEntity<RentalsResponse> getAllRentals() {
        try {
            log.info("Récupération de toutes les locations");
            List<RentalEntity> rentals = rentalService.getAllRentals();
            List<RentalDTO> rentalDTOs = rentals.stream()
                                                 .map(RentalMapper::toDTO)
                                                 .collect(Collectors.toList());

            RentalsResponse response = new RentalsResponse();
            response.setRentals(rentalDTOs);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des locations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupérer une location par ID.
     * @param id ID de la location.
     * @return ResponseEntity contenant la location ou un statut 404 si non trouvée.
     */
    @Operation(summary = "Obtenir une location par ID", description = "Retourne une location spécifique par son ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location trouvée"),
        @ApiResponse(responseCode = "404", description = "Location non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(
            @Parameter(description = "ID de la location", example = "1") @PathVariable Integer id) {
        log.info("Récupération de la location ID: {}", id);
        RentalEntity rental = rentalService.getRentalById(id);
        if (rental != null) {
            return ResponseEntity.ok(RentalMapper.toDTO(rental));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Créer une nouvelle location.
     * @param name Nom de la location.
     * @param surface Surface de la location.
     * @param price Prix de la location.
     * @param description Description de la location.
     * @param picture Image associée à la location.
     * @return ResponseEntity contenant un message de succès ou un message d'erreur.
     */
    @Operation(summary = "Créer une nouvelle location", description = "Ajoute une nouvelle location avec ses détails.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location créée avec succès"),
        @ApiResponse(responseCode = "500", description = "Erreur interne lors de la création de la location")
    })
    @PostMapping
    public ResponseEntity<?> createRental(
        @RequestParam("name") String name,
        @RequestParam("surface") Double surface,
        @RequestParam("price") Double price,
        @RequestParam("description") String description,
        @RequestParam(value = "picture", required = false) MultipartFile picture) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        UserEntity owner = userService.getUserByEmail(currentUserEmail);

        if (owner == null) {
            log.warn("Utilisateur non trouvé: {}", currentUserEmail);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        log.info("Création d'une location par l'utilisateur: {}", currentUserEmail);
        RentalEntity rentalEntity = new RentalEntity();
        rentalEntity.setName(name);
        rentalEntity.setOwner(owner);
        rentalEntity.setSurface(surface);
        rentalEntity.setPrice(price);
        rentalEntity.setDescription(description);

        if (picture != null && !picture.isEmpty()) {
            String fileName = picture.getOriginalFilename();
            Path path = Paths.get("uploads/" + fileName);
            try {
                Files.copy(picture.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                rentalEntity.setPicture("uploads/" + fileName);
            } catch (IOException e) {
                log.error("Erreur lors de l'enregistrement de l'image", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving picture.");
            }
        }

        try {
            rentalService.createRental(rentalEntity);
        } catch (Exception e) {
            log.error("Erreur lors de la création de la location", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating rental.");
        }

        return ResponseEntity.ok().body(Map.of("message", "Rental created!"));
    }

    /**
     * Mettre à jour une location existante.
     * @param id ID de la location à mettre à jour.
     * @param name Nom de la location.
     * @param surface Surface de la location.
     * @param price Prix de la location.
     * @param description Description de la location.
     * @return ResponseEntity indiquant le succès ou l'échec de la mise à jour.
     */
    @Operation(summary = "Mettre à jour une location", description = "Met à jour une location existante.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location mise à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Location non trouvée")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRental(
            @Parameter(description = "ID de la location", example = "1") @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("surface") Double surface,
            @RequestParam("price") Double price,
            @RequestParam("description") String description) {

        // Vérifier si la location existe
        Optional<RentalEntity> rentalOpt = Optional.ofNullable(rentalService.getRentalById(id));

        if (!rentalOpt.isPresent()) {
            log.warn("Location non trouvée pour l'ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rental not found.");
        }

        // Récupérer l'entité location existante
        RentalEntity rentalEntity = rentalOpt.get();
        
        // Mettre à jour les champs de la location
        rentalEntity.setName(name);
        rentalEntity.setSurface(surface);
        rentalEntity.setPrice(price);
        rentalEntity.setDescription(description);

        // Mise à jour dans le service en passant l'ID et l'objet RentalEntity
        try {
            rentalService.updateRental(id, rentalEntity);  // Mise à jour de la location avec l'ID et l'objet
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour de la location", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating rental.");
        }

        // Retourner une réponse de succès
        log.info("Location mise à jour avec succès pour l'ID: {}", id);
        return ResponseEntity.ok().body(Map.of("message", "Rental updated!"));
    }


    /**
     * Supprimer une location par ID.
     * @param id ID de la location à supprimer.
     * @return ResponseEntity avec un statut indiquant le succès ou l'échec.
     */
    @Operation(summary = "Supprimer une location", description = "Supprime une location par son ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Location supprimée avec succès"),
        @ApiResponse(responseCode = "404", description = "Location non trouvée")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(
            @Parameter(description = "ID de la location", example = "1") @PathVariable Integer id) {
        boolean isDeleted = rentalService.deleteRental(id);

        if (isDeleted) {
            log.info("Location supprimée pour l'ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Location non trouvée pour l'ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
