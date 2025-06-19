package com.techcmr.tech_cmr.repository;

import com.techcmr.tech_cmr.model.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @EntityGraph(attributePaths = {"tasks", "projects"})
    Optional<Tag> findWithTasksAndProjectsById(Long id);
}
