package com.projet3.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.projet3.configuration.JwtUtil;
import com.projet3.dto.LoginDTO;
import com.projet3.dto.RegisterDTO;
import com.projet3.dto.UserDTO;
import com.projet3.entity.UserEntity;
import com.projet3.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Map;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    public AuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Méthode pour enregistrer un nouvel utilisateur
    @Operation(summary = "Enregistrer un nouvel utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateur enregistré avec succès"),
        @ApiResponse(responseCode = "400", description = "Email déjà pris")
    })
    public ResponseEntity<?> register(RegisterDTO userDTO) {
        logger.info("Tentative d'enregistrement de l'utilisateur: {}", userDTO.getEmail());

        // Vérification si l'email existe déjà
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            logger.warn("Un utilisateur avec l'email {} existe déjà", userDTO.getEmail());
            return ResponseEntity.status(400).body(Map.of("message", "Email already taken"));
        }
        UserEntity user = new UserEntity();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setName(userDTO.getName());
        userRepository.save(user);

        logger.info("Utilisateur enregistré avec succès: {}", userDTO.getEmail());
        return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
    }

    // Méthode pour se connecter
    @Operation(summary = "Connexion d'un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Connexion réussie"),
        @ApiResponse(responseCode = "401", description = "Mot de passe incorrect ou utilisateur non trouvé")
    })
    public ResponseEntity<?> login(LoginDTO userDTO) {
        logger.info("Début de la tentative de connexion pour l'utilisateur: {}", userDTO.getEmail());

        UserEntity user = userRepository.findByEmail(userDTO.getEmail());
        if (user != null) {
            logger.info("Utilisateur trouvé: {}", userDTO.getEmail());
            if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                logger.info("Mot de passe valide pour l'utilisateur: {}", userDTO.getEmail());

                String token = jwtUtil.generateToken(user.getEmail());
                logger.info("Token JWT généré avec succès pour l'utilisateur: {}", userDTO.getEmail());

                return ResponseEntity.ok(new AuthSuccess(token));
            } else {
                logger.warn("Mot de passe invalide pour l'utilisateur: {}", userDTO.getEmail());
                return ResponseEntity.status(401).body(Map.of("error", "Incorrect password"));
            }
        } else {
            logger.warn("Aucun utilisateur trouvé avec l'email: {}", userDTO.getEmail());
            return ResponseEntity.status(401).body(Map.of("error", "User not found"));
        }
    }

    // Nouvelle méthode pour récupérer les détails de l'utilisateur authentifié par email
    @Operation(summary = "Récupérer les détails de l'utilisateur par email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Détails de l'utilisateur récupérés avec succès"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public UserDTO getUserDetailsByEmail(String email) {
        logger.info("Récupération des détails de l'utilisateur pour l'email: {}", email);

        // Recherche de l'utilisateur par son email dans le repository
        UserEntity user = userRepository.findByEmail(email);

        if (user != null) {
            // Mapper l'entité utilisateur en DTO (UserDTO) pour l'envoyer à l'API
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(user.getEmail());
            userDTO.setName(user.getName());
            // Ajoutez d'autres champs nécessaires ici si besoin
            return userDTO;
        } else {
            // Si l'utilisateur n'existe pas dans la base de données
            logger.warn("Utilisateur non trouvé pour l'email: {}", email);
            return null;  // Retourne null si l'utilisateur n'est pas trouvé
        }
    }

    // Classe interne pour la réponse d'authentification
    public static class AuthSuccess {
        private String token;

        public AuthSuccess(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
