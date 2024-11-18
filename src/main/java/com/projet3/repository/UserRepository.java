package com.projet3.repository;

import com.projet3.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Recherche un utilisateur par son ID.
     * 
     * @param id L'ID de l'utilisateur à rechercher.
     * @return Un utilisateur correspondant à l'ID.
     */
    @Operation(summary = "Trouver un utilisateur par ID", 
               description = "Cette méthode permet de récupérer un utilisateur en fonction de son ID.")
    UserEntity findById(
        @Parameter(description = "L'ID de l'utilisateur à récupérer.", required = true) int id);

    /**
     * Recherche un utilisateur par son email.
     * 
     * @param email L'email de l'utilisateur à rechercher.
     * @return Un utilisateur correspondant à l'email.
     */
    @Operation(summary = "Trouver un utilisateur par email", 
               description = "Cette méthode permet de récupérer un utilisateur en fonction de son email.")
    UserEntity findByEmail(
        @Parameter(description = "L'email de l'utilisateur à récupérer.", required = true) String email);

    /**
     * Recherche un utilisateur par son nom.
     * 
     * @param name Le nom de l'utilisateur à rechercher.
     * @return Un utilisateur correspondant au nom.
     */
    @Operation(summary = "Trouver un utilisateur par nom", 
               description = "Cette méthode permet de récupérer un utilisateur en fonction de son nom.")
    UserEntity findByName(
        @Parameter(description = "Le nom de l'utilisateur à récupérer.", required = true) String name);
}
