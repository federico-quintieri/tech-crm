package com.techcmr.tech_cmr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techcmr.tech_cmr.model.User;

// Repository entity User con chiave primaria Long
public interface UserRepository extends JpaRepository<User, Long> {

    // Metodo custom jpa per trovare user in base ad username
    <Optional> User findByUsername(String username);
}