package com.techcmr.tech_cmr.repository;

import com.techcmr.tech_cmr.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
