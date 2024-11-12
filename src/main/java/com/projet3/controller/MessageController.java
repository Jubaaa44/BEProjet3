package com.projet3.controller;

import com.projet3.dto.MessageDTO;
import com.projet3.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "Obtenir les messages d'un utilisateur",
               description = "Retourne la liste des messages associés à l'utilisateur spécifié.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MessageDTO>> getUserMessages(
            @Parameter(description = "ID de l'utilisateur", example = "1") @PathVariable Integer userId) {
        List<MessageDTO> messages = messageService.getMessagesByUserId(userId);
        return ResponseEntity.ok(messages);
    }

    @Operation(summary = "Créer un message",
            description = "Ajoute un nouveau message.")
	 @PostMapping("/")
	 public ResponseEntity<Map<String, String>> createMessage(@RequestBody MessageDTO messageDTO) {
	     messageService.createMessage(messageDTO);
	     return ResponseEntity.ok().body(Map.of("message", "Message sent with success"));
	 }

}
