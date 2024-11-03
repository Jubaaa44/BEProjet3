package com.projet3.repository;

import org.springframework.stereotype.Repository;

import com.projet3.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
