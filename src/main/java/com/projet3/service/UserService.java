package com.projet3.service;

import org.springframework.stereotype.Service;

import com.projet3.entity.UserEntity;
import com.projet3.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
 // Méthode pour obtenir un utilisateur par son ID
    public UserEntity getUserById(Long id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        return userOptional.orElse(null); // Renvoie l'utilisateur si trouvé, sinon null
    }
}
