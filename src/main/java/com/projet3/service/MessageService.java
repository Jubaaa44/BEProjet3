package com.projet3.service;

import com.projet3.dto.MessageDTO;
import com.projet3.entity.MessageEntity;
import com.projet3.entity.RentalEntity;
import com.projet3.entity.UserEntity;
import com.projet3.mapper.MessageMapper; // Mapper pour convertir entre Entity et DTO
import com.projet3.repository.MessageRepository;
import com.projet3.repository.RentalRepository; // Ajoutez le repository de Rental
import com.projet3.repository.UserRepository; // Ajoutez le repository de User
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;

    // Méthode pour récupérer les messages d'un utilisateur
    public List<MessageDTO> getMessagesByUserId(Integer userId) {
        List<MessageEntity> messages = messageRepository.findByUserId(userId);
        return messages.stream()
                       .map(MessageMapper::toDTO) // Convertit l'entité en DTO
                       .collect(Collectors.toList());
    }

 // Méthode pour créer un message
    public void createMessage(MessageDTO messageDTO) {
        // Convertir le DTO en Entity
        MessageEntity message = MessageMapper.toEntity(messageDTO);
        // Définir la date de création
        message.setCreatedAt(LocalDateTime.now());
        // Enregistrer le message dans la base de données
        messageRepository.save(message);
    }
}
