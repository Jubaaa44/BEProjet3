package com.projet3.repository;

import com.projet3.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {
    List<MessageEntity> findByUserId(Integer userId); // Méthode pour trouver des messages par ID utilisateur
}
