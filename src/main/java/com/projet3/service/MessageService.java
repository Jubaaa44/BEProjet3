package com.projet3.service;

import com.projet3.dto.MessageDTO;
import com.projet3.entity.MessageEntity;
import com.projet3.mapper.MessageMapper; // Mapper pour convertir entre Entity et DTO
import com.projet3.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;

    public List<MessageDTO> getMessagesByUserId(Integer userId) {
        List<MessageEntity> messages = messageRepository.findByUserId(userId);
        return messages.stream()
                       .map(MessageMapper::toDTO) // Convertit l'entité en DTO
                       .collect(Collectors.toList());
    }

    public void createMessage(MessageDTO messageDTO) {
        MessageEntity message = new MessageEntity();
        message.setMessage(messageDTO.getMessage());
        message.setCreatedAt(LocalDateTime.now());
        // Remplissez l'ID de location et l'ID d'utilisateur si nécessaire
        // message.setRental(new RentalEntity(messageDTO.getRentalId())); // Si RentalEntity est une entité existante
        // message.setUser(new UserEntity(messageDTO.getUserId())); // Si UserEntity est une entité existante
        messageRepository.save(message);
    }
}
