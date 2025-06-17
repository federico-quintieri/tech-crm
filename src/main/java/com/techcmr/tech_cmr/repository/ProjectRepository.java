package com.techcmr.tech_cmr.repository;

import com.techcmr.tech_cmr.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
