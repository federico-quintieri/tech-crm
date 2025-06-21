package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.TaskDTO;
import com.techcmr.tech_cmr.mapper.TaskMapper;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    // READ
    public List<TaskDTO> findAllTasks() {
        return taskRepository.findAll().stream().map(taskMapper::toDto).toList();
    }

    // SHOW
    public TaskDTO findTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        return taskMapper.toDto(task);
    }

    // CREATE
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = taskMapper.toEntity(taskDTO);
        Task savedTask =taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    // UPDATE
    public void updateTask(Long id, TaskDTO taskDTO) {
        Task updatedTask = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found"));

        taskMapper.updateEntityFromDto(taskDTO, updatedTask);

        taskRepository.save(updatedTask);
    }

    // DELETE
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        taskRepository.delete(task);
    }

}
