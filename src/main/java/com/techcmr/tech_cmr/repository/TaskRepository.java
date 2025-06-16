package com.techcmr.tech_cmr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techcmr.tech_cmr.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}