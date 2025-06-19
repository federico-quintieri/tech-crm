package com.techcmr.tech_cmr.repository;

import com.techcmr.tech_cmr.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT t FROM Tag t LEFT JOIN FETCH t.tasks LEFT JOIN FETCH t.projects WHERE t.id = :id")
    Optional<Tag> findWithTasksAndProjectsById(Long id);
}
