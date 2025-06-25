package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.TaskDTO;
import com.techcmr.tech_cmr.mapper.TaskMapper;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import com.techcmr.tech_cmr.repository.SectionRepository;
import com.techcmr.tech_cmr.repository.TagRepository;
import com.techcmr.tech_cmr.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private TaskMapper taskMapper;

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    // READ
    @Transactional(readOnly = true)
    public List<TaskDTO> findAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDto)
                .toList();
    }

    // SHOW
    @Transactional(readOnly = true)
    public TaskDTO findTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        return taskMapper.toDto(task);
    }

    // CREATE
    public TaskDTO createTask(TaskDTO taskDTO) {
        // Impostiamo la data di creazione
        Task task = taskMapper.toEntity(taskDTO,projectRepository,sectionRepository,tagRepository);
        task.setCreatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    // UPDATE
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        taskMapper.updateEntityFromDto(taskDTO, existingTask, projectRepository,sectionRepository,tagRepository);

        // Se il task viene completato, impostiamo la data di completamento
        if (taskDTO.getStatus() != null &&
                taskDTO.getStatus().name().equals("COMPLETED") &&
                existingTask.getCompletedAt() == null) {
            existingTask.setCompletedAt(LocalDateTime.now());
        }

        Task savedTask = taskRepository.save(existingTask);
        return taskMapper.toDto(savedTask);
    }

    // DELETE
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    // Metodi di utilità aggiuntivi

    @Transactional(readOnly = true)
    public List<TaskDTO> findTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> findTasksBySectionId(Long sectionId) {
        return taskRepository.findBySectionId(sectionId).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }
}