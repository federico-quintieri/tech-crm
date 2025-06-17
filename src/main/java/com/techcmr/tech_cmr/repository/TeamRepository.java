package com.techcmr.tech_cmr.repository;

import com.techcmr.tech_cmr.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
