package com.projet3.controller;

import com.projet3.dto.RentalDTO;
import com.projet3.entity.RentalEntity;
import com.projet3.entity.UserEntity;
import com.projet3.mapper.RentalMapper;
import com.projet3.repository.UserRepository;
import com.projet3.response.RentalsResponse;
import com.projet3.service.RentalService;
import com.projet3.service.UserService;

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
public class RentalController {

    @Autowired
    private RentalService rentalService;
    
    @Autowired
    private UserService userService;
    
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @GetMapping
    public ResponseEntity<RentalsResponse> getAllRentals() {
        try {
            List<RentalEntity> rentals = rentalService.getAllRentals();
            List<RentalDTO> rentalDTOs = rentals.stream()
                                                 .map(RentalMapper::toDTO)
                                                 .collect(Collectors.toList());

            RentalsResponse response = new RentalsResponse();
            response.setRentals(rentalDTOs);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    // Récupérer une location par ID
    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Integer id) {
        RentalEntity rental = rentalService.getRentalById(id);
        if (rental != null) {
            return ResponseEntity.ok(RentalMapper.toDTO(rental));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createRental(
        @RequestParam("name") String name,
        @RequestParam("surface") Double surface,
        @RequestParam("price") Double price,
        @RequestParam("description") String description,
        @RequestParam(value = "picture", required = false) MultipartFile picture) {

        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Supposant que l'e-mail est utilisé comme nom d'utilisateur

        // Rechercher l'utilisateur dans la base de données
        UserEntity owner = userService.getUserByEmail(currentUserEmail);
        if (owner == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        // Créer une instance de RentalEntity avec les données du formulaire
        RentalEntity rentalEntity = new RentalEntity();
        rentalEntity.setName(name);
        rentalEntity.setOwner(owner); // Associer l'utilisateur comme propriétaire
        rentalEntity.setSurface(surface);
        rentalEntity.setPrice(price);
        rentalEntity.setDescription(description);

        // Si une image est envoyée, la traiter
        if (picture != null && !picture.isEmpty()) {
            String fileName = picture.getOriginalFilename();
            Path path = Paths.get("uploads/" + fileName);
            try {
                Files.copy(picture.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                rentalEntity.setPicture("uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving picture.");
            }
        }

        // Appeler le service pour enregistrer la location dans la base de données
        try {
            rentalService.createRental(rentalEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating rental.");
        }

        // Retourner une réponse de succès
        return ResponseEntity.ok().body(Map.of("message", "Rental created!"));
    }




 // Mettre à jour une location existante
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRental(@PathVariable Integer id, @RequestBody RentalDTO rentalDTO) {
        // Convertir RentalDTO en RentalEntity
        RentalEntity rentalEntity = RentalMapper.toEntity(rentalDTO);
        
        // Tenter de mettre à jour la location
        Optional<RentalEntity> updatedRental = rentalService.updateRental(id, rentalEntity);
        
        if (updatedRental != null) {
            // Si la location a été mise à jour avec succès
            return ResponseEntity.ok().body(Map.of("message", "Rental updated!"));
        } else {
            // Si la location n'existe pas, renvoyer un statut 404
            return ResponseEntity.notFound().build();
        }
    }



    // Supprimer une location
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Integer id) {
        boolean isDeleted = rentalService.deleteRental(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
