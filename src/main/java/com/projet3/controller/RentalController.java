package com.projet3.controller;

import com.projet3.dto.RentalDTO;
import com.projet3.entity.RentalEntity;
import com.projet3.mapper.RentalMapper;
import com.projet3.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Récupérer toutes les locations
    @GetMapping
    public List<RentalDTO> getAllRentals() {
        List<RentalEntity> rentals = rentalService.getAllRentals();
        return rentals.stream().map(RentalMapper::toDTO).collect(Collectors.toList());
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

    // Créer une nouvelle location
    @PostMapping
    public ResponseEntity<?> createRental(@RequestBody RentalDTO rentalDTO) {
        RentalEntity rentalEntity = RentalMapper.toEntity(rentalDTO);
        RentalEntity createdRental = rentalService.createRental(rentalEntity);
        
        // Message de confirmation pour la création
        return ResponseEntity.ok().body(Map.of("message", "Rental created!"));
    }


 // Mettre à jour une location existante
    @PutMapping("/{id}")
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
