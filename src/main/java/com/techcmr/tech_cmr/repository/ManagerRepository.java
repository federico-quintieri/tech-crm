package com.techcmr.tech_cmr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techcmr.tech_cmr.model.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

}