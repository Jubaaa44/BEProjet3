package com.projet3.controller;

import java.security.Principal;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.projet3.configuration.CustomUserDetailsService;
import com.projet3.dto.UserDTO;
import com.projet3.entity.UserEntity;
import com.projet3.repository.UserRepository;
import com.projet3.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    // Route pour l'inscription
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return authService.register(userDTO);
    }

    // Route pour la connexion
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            log.info("Tentative de connexion pour l'email: {}", userDTO.getEmail());
            return authService.login(userDTO);
        } catch (RuntimeException e) {
            log.error("Erreur dans le processus de login pour l'email: {}", userDTO.getEmail(), e);
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
/*
    @GetMapping("/me")
    public UserDTO me(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        log.info(principal.getName());
        UserDTO userDTO = new UserDTO();

        // Récupérer l'utilisateur si authentifié
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String email = userDetails.getUsername();
            log.info("Récupération de l'utilisateur : {}", email);

            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) {
                log.error("Utilisateur non trouvé pour l'email : {}", email);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
            }

            // Créer le DTO et le renvoyer
            userDTO.setEmail(userEntity.getEmail());
            userDTO.setName(userEntity.getName());
            userDTO.setCreated_at(userEntity.getCreatedAt());
            userDTO.setUpdated_at(userEntity.getUpdatedAt());
        } else {
            log.error("Principal n'est pas une instance de UserDetails");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié");
        }

        return userDTO;
    }*/
    
    @GetMapping("/me")
    public UserDTO me(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        
        if (principal == null || principal.getName() == null) {
            log.error("Utilisateur non authentifié");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié");
        }

        log.info("Récupération de l'utilisateur : {}", principal.getName());
        String email = principal.getName();

        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            log.error("Utilisateur non trouvé pour l'email : {}", email);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
        }

        // Créer le DTO et le renvoyer
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setName(userEntity.getName());
        userDTO.setCreated_at(userEntity.getCreatedAt());
        userDTO.setUpdated_at(userEntity.getUpdatedAt());

        return userDTO;
    }


}
