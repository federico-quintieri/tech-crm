package com.techcmr.tech_cmr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techcmr.tech_cmr.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

}