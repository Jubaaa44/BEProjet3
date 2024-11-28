package com.projet3.service;

import com.projet3.entity.RentalEntity;
import com.projet3.repository.RentalRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    private static final String UPLOAD_DIR = "uploads/"; // Dossier où les fichiers seront stockés

    // Récupérer toutes les locations
    public List<RentalEntity> getAllRentals() {
        return rentalRepository.findAll();
    }

    // Récupérer une location par ID
    public RentalEntity getRentalById(Integer id) {
        Optional<RentalEntity> rental = rentalRepository.findById(id);
        return rental.orElse(null);
    }

    // Créer une nouvelle location avec gestion des fichiers
    public RentalEntity createRental(RentalEntity rentalEntity, MultipartFile picture) {
        if (picture != null && !picture.isEmpty()) {
            try {
                // Générer un nom de fichier unique
                String originalFileName = picture.getOriginalFilename();
                String extension = originalFileName != null && originalFileName.contains(".") 
                    ? originalFileName.substring(originalFileName.lastIndexOf(".")) 
                    : "";
                String uniqueFileName = UUID.randomUUID().toString() + extension;

                // Enregistrer le fichier sur le serveur
                Path uploadPath = Paths.get(UPLOAD_DIR + uniqueFileName);
                Files.copy(picture.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

                // Enregistrer le chemin du fichier dans l'entité
                rentalEntity.setPicture(UPLOAD_DIR + uniqueFileName);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de l'enregistrement de l'image", e);
            }
        }

        // Sauvegarder la location dans la base de données
        return rentalRepository.save(rentalEntity);
    }

    // Mettre à jour une location avec gestion des fichiers
    public Optional<RentalEntity> updateRental(Integer id, RentalEntity rentalDetails, MultipartFile picture) {
        Optional<RentalEntity> existingRentalOpt = rentalRepository.findById(id);
        if (existingRentalOpt.isPresent()) {
            RentalEntity existingRental = existingRentalOpt.get();
            
            // Mettre à jour les propriétés existantes
            existingRental.setName(rentalDetails.getName());
            existingRental.setSurface(rentalDetails.getSurface());
            existingRental.setPrice(rentalDetails.getPrice());
            existingRental.setDescription(rentalDetails.getDescription());

            // Gérer la mise à jour de l'image (si présente)
            if (picture != null && !picture.isEmpty()) {
                try {
                    // Générer un nom de fichier unique
                    String originalFileName = picture.getOriginalFilename();
                    String extension = originalFileName != null && originalFileName.contains(".") 
                        ? originalFileName.substring(originalFileName.lastIndexOf(".")) 
                        : "";
                    String uniqueFileName = UUID.randomUUID().toString() + extension;

                    // Enregistrer le fichier sur le serveur
                    Path uploadPath = Paths.get(UPLOAD_DIR + uniqueFileName);
                    Files.copy(picture.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

                    // Mettre à jour le chemin de l'image
                    existingRental.setPicture(UPLOAD_DIR + uniqueFileName);
                } catch (IOException e) {
                    throw new RuntimeException("Erreur lors de l'enregistrement de l'image", e);
                }
            }

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
