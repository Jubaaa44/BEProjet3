package com.projet3.service;

import org.springframework.stereotype.Service;

import com.projet3.dto.UserDTO;
import com.projet3.entity.UserEntity;
import com.projet3.mapper.UserMapper;
import com.projet3.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // Méthode pour trouver un utilisateur par son id
    public UserDTO getUserById(int id) {
    	UserEntity userEntity = userRepository.findById(id);
    	return UserMapper.toDTO(userEntity);
    }
    
    // Méthode pour trouver un utilisateur par son email
    public UserEntity getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        return userEntity;
    }
    
    // Méthode pour trouver un utilisateur par son nom
    public UserDTO getUserByName(String name) {
        UserEntity userEntity = userRepository.findByName(name);
        return UserMapper.toDTO(userEntity);
    }
    
    public UserEntity createUser(UserDTO userDTO) {
        UserEntity userEntity = UserMapper.toEntity(userDTO);
        return userRepository.save(userEntity);
    }
    
}
