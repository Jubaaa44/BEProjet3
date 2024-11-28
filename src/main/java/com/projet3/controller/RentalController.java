package com.projet3.controller;

import com.projet3.entity.RentalEntity;
import com.projet3.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    // Récupérer toutes les locations
    @GetMapping
    public ResponseEntity<List<RentalEntity>> getAllRentals() {
        List<RentalEntity> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals);
    }

    // Récupérer une location par son ID
    @GetMapping("/{id}")
    public ResponseEntity<RentalEntity> getRentalById(@PathVariable Integer id) {
        RentalEntity rental = rentalService.getRentalById(id);
        return rental != null ? ResponseEntity.ok(rental) :
                               ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Créer une nouvelle location avec possibilité d'ajouter un fichier
    @PostMapping
    public ResponseEntity<?> createRental(
        @RequestParam("name") String name,
        @RequestParam("surface") Double surface,
        @RequestParam("price") Double price,
        @RequestParam("description") String description,
        @RequestParam(value = "picture", required = false) MultipartFile picture) {

        RentalEntity rentalEntity = new RentalEntity();
        rentalEntity.setName(name);
        rentalEntity.setSurface(surface);
        rentalEntity.setPrice(price);
        rentalEntity.setDescription(description);

        try {
            rentalService.createRental(rentalEntity, picture);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Rental created successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    // Mettre à jour une location avec gestion des fichiers
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRental(
        @PathVariable Integer id,
        @RequestParam("name") String name,
        @RequestParam("surface") Double surface,
        @RequestParam("price") Double price,
        @RequestParam("description") String description,
        @RequestParam(value = "picture", required = false) MultipartFile picture) {

        RentalEntity rentalEntity = new RentalEntity();
        rentalEntity.setName(name);
        rentalEntity.setSurface(surface);
        rentalEntity.setPrice(price);
        rentalEntity.setDescription(description);

        try {
            Optional<RentalEntity> updatedRental = rentalService.updateRental(id, rentalEntity, picture);
            return updatedRental.map(r -> ResponseEntity.ok("Rental updated successfully!"))
                                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rental not found."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    // Supprimer une location
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRental(@PathVariable Integer id) {
        boolean isDeleted = rentalService.deleteRental(id);
        return isDeleted ? ResponseEntity.ok(Map.of("message", "Rental deleted successfully!"))
                         : ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Rental not found"));
    }
}
