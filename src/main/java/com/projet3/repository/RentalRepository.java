package com.projet3.repository;

import com.projet3.entity.RentalEntity;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<RentalEntity, Integer> {
	
}
	
