package com.projet3.controller;

import com.projet3.dto.MessageDTO;
import com.projet3.entity.UserEntity;
import com.projet3.response.MessageRequest;
import com.projet3.response.MessageResponse;
import com.projet3.service.MessageService;
import com.projet3.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Message Controller", description = "Gestion des messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    /**
     * Récupérer les messages d'un utilisateur spécifique.
     * @param userId ID de l'utilisateur.
     * @return Liste des messages associés à l'utilisateur.
     */
    @Operation(summary = "Obtenir les messages d'un utilisateur",
               description = "Retourne la liste des messages associés à l'utilisateur spécifié.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Messages récupérés avec succès"),
        @ApiResponse(responseCode = "404", description = "Utilisateur ou messages non trouvés")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MessageDTO>> getUserMessages(
            @Parameter(description = "ID de l'utilisateur", example = "1") @PathVariable Integer userId) {
        log.info("Récupération des messages pour l'utilisateur ID: {}", userId);
        List<MessageDTO> messages = messageService.getMessagesByUserId(userId);
        return ResponseEntity.ok(messages);
    }

    /**
     * Créer un nouveau message pour un utilisateur authentifié.
     * @param mr Requête contenant les détails du message.
     * @return MessageResponse indiquant le succès de la création.
     */
    @Operation(summary = "Créer un message", description = "Ajoute un nouveau message.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Message créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(@RequestBody MessageRequest mr) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        UserEntity userConnected = userRepository.findByEmail(currentUserEmail);

        log.info("Création d'un message par l'utilisateur: {}", currentUserEmail);

        // Créer un DTO pour le message
        MessageDTO dto = new MessageDTO();
        dto.setMessage(mr.getMessage());
        dto.setRentalId(mr.getRental_id());
        dto.setUserId(userConnected.getId());

        // Appeler le service pour créer le message
        messageService.createMessage(dto);

        // Créer et retourner la réponse
        MessageResponse response = new MessageResponse();
        response.setMessage("Message sent with success");

        log.info("Message créé avec succès par l'utilisateur ID: {}", userConnected.getId());
        return ResponseEntity.ok(response);
    }
}
