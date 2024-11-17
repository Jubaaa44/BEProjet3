package com.projet3.controller;

import com.projet3.dto.MessageDTO;
import com.projet3.entity.UserEntity;
import com.projet3.mapper.MessageMapper;
import com.projet3.repository.UserRepository;
import com.projet3.service.MessageService;
import com.projet3.response.MessageRequest;
import com.projet3.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Message Controller", description = "Gestion des messages")
public class MessageController {

    @Autowired
    private MessageService messageService;
    
    @Autowired 
    private UserRepository userRepository;
    
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    // Récupérer les messages d'un utilisateur
    @Operation(summary = "Obtenir les messages d'un utilisateur",
               description = "Retourne la liste des messages associés à l'utilisateur spécifié.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MessageDTO>> getUserMessages(
            @Parameter(description = "ID de l'utilisateur", example = "1") @PathVariable Integer userId) {
        List<MessageDTO> messages = messageService.getMessagesByUserId(userId);
        return ResponseEntity.ok(messages);
    }

    // Créer un message
    @Operation(summary = "Créer un message",
               description = "Ajoute un nouveau message.")
    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(@RequestBody MessageRequest mr) {
        
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        UserEntity userConnected = userRepository.findByEmail(currentUserEmail);
        
        MessageDTO dto = new MessageDTO();
        dto.setMessage(mr.getMessage());
        dto.setRentalId(mr.getRental_id());
        dto.setUserId(userConnected.getId());
        messageService.createMessage(dto);
    	
     // Créer une instance de MessageResponse
        MessageResponse response = new MessageResponse();
        response.setMessage("Message sent with success");
        
        // Retourner la réponse avec MessageResponse
        return ResponseEntity.ok(response);
    }
}
