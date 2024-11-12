package com.projet3.service;

import com.projet3.entity.RentalEntity;
import com.projet3.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    // Récupérer toutes les locations
    public List<RentalEntity> getAllRentals() {
        return rentalRepository.findAll();
    }

    // Récupérer une location par ID
    public RentalEntity getRentalById(Integer id) {
        Optional<RentalEntity> rental = rentalRepository.findById(id);
        return rental.orElse(null);
    }

    // Créer une nouvelle location
    public RentalEntity createRental(RentalEntity rentalEntity) {
        return rentalRepository.save(rentalEntity);
    }

    // Mettre à jour une location existante
    public Optional<RentalEntity> updateRental(Integer id, RentalEntity rentalDetails) {
        Optional<RentalEntity> existingRentalOpt = rentalRepository.findById(id);
        if (existingRentalOpt.isPresent()) {
            RentalEntity existingRental = existingRentalOpt.get();
            // Mettre à jour les propriétés existantes avec celles de rentalDetails
            existingRental.setName(rentalDetails.getName());
            existingRental.setSurface(rentalDetails.getSurface());
            existingRental.setPrice(rentalDetails.getPrice());
            existingRental.setPicture(rentalDetails.getPicture());
            existingRental.setDescription(rentalDetails.getDescription());
            
            // Sauvegarder les modifications
            rentalRepository.save(existingRental);
            return Optional.of(existingRental); // Retourner l'entité mise à jour
        } else {
            return Optional.empty(); // Location non trouvée
        }
    }



    // Supprimer une location
    public boolean deleteRental(Integer id) {
        Optional<RentalEntity> rental = rentalRepository.findById(id);
        if (rental.isPresent()) {
            rentalRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
