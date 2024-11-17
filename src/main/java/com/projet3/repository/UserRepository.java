package com.projet3.repository;

import org.springframework.stereotype.Repository;

import com.projet3.entity.UserEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	// Méthode pour rechercher par id
	UserEntity findById(int id);
	
    // Méthode pour trouver un utilisateur par son email
    UserEntity findByEmail(String email);
    
    // Méthode pour rechercher par nom
    UserEntity findByName(String name);
    
}
