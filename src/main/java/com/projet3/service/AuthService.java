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

    public ResponseEntity<?> register(RegisterDTO userDTO) {
        logger.info("Tentative d'enregistrement de l'utilisateur: {}", userDTO.getEmail());
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            return ResponseEntity.status(400).body(Map.of("message", "Email already taken"));
        }
        UserEntity user = new UserEntity();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setName(userDTO.getName());
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
    }

    public ResponseEntity<?> login(LoginDTO userDTO) {
        logger.info("Début de la tentative de connexion pour l'utilisateur: {}", userDTO.getEmail());
        UserEntity user = userRepository.findByEmail(userDTO.getEmail());
        if (user == null || !passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthSuccess(token));
    }

    public UserDTO getUserDetailsByEmail(String email) {
        logger.info("Récupération des détails de l'utilisateur pour l'email: {}", email);
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setCreated_at(user.getCreatedAt());
        userDTO.setUpdated_at(user.getUpdatedAt());
        return userDTO;
    }

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
