package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.TaskDTO;
import com.techcmr.tech_cmr.mapper.TaskMapper;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.relations.TaskRelationManager;
import com.techcmr.tech_cmr.repository.AttachmentRepository;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import com.techcmr.tech_cmr.repository.SectionRepository;
import com.techcmr.tech_cmr.repository.StoryRepository;
import com.techcmr.tech_cmr.repository.TagRepository;
import com.techcmr.tech_cmr.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private StoryRepository storyRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskRelationManager taskRelationManager;

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
        // 1
        Task task = taskMapper.toEntity(taskDTO, tagRepository, projectRepository, sectionRepository, storyRepository,
                attachmentRepository);

        // 2
        taskRelationManager.updateRelationsForPostTask(task);

        // 3
        Task savedTask = taskRepository.save(task);
        // 4
        return taskMapper.toDto(savedTask);
    }

    // UPDATE
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {

        // 1
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        // 2
        taskMapper.updateEntityFromDto(taskDTO, existingTask, projectRepository, sectionRepository, tagRepository);

        // 3
        taskRelationManager.updateAttachmentsForTask(existingTask, taskDTO);
        taskRelationManager.updateStoriesForTask(existingTask, taskDTO);

        // 4
        Task savedTask = taskRepository.save(existingTask);
        // 5
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