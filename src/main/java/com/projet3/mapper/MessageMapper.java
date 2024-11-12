package com.projet3.mapper;

import com.projet3.dto.MessageDTO;
import com.projet3.entity.MessageEntity;

public class MessageMapper {
    public static MessageDTO toDTO(MessageEntity message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setMessage(message.getMessage());
        dto.setRentalId(message.getRental() != null ? message.getRental().getId() : null);
        dto.setUserId(message.getUser() != null ? message.getUser().getId() : null);
        return dto;
    }

    public static MessageEntity toEntity(MessageDTO dto) {
        MessageEntity message = new MessageEntity();
        message.setId(dto.getId());
        message.setMessage(dto.getMessage());
        return message;
    }
}
