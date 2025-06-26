package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.ProjectDTO;
import com.techcmr.tech_cmr.mapper.ProjectMapper;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import com.techcmr.tech_cmr.repository.TagRepository;
import com.techcmr.tech_cmr.repository.TaskRepository;
import com.techcmr.tech_cmr.repository.TeamRepository;
import com.techcmr.tech_cmr.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    // Use repositories directly instead of services to avoid circular dependency
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProjectMapper projectMapper;

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    // READ
    public List<ProjectDTO> findAllProjects() {
        return projectRepository.findAll().stream().map(projectMapper::toDto).toList();
    }

    // READ
    public ProjectDTO findProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return projectMapper.toDto(project);
    }

    // CREATE
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        // Use repositories instead of services
        Project project = projectMapper.toEntity(projectDTO, teamRepository, workspaceRepository,
                taskRepository, tagRepository);

        // Set bidirectional relationship for tasks
        if (project.getTasks() != null) {
            for (Task task : project.getTasks()) {
                task.setProject(project);
            }
        }

        Project savedProject = projectRepository.save(project);
        return projectMapper.toDto(savedProject);
    }

    // UPDATE
    public void updateProject(Long id, ProjectDTO projectDTO) {

        // 1. Trova il project esistente
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        // 2. Mergiare l'entity alla DTO
        projectMapper.updateEntityFromDto(projectDTO, existingProject, teamRepository,
                workspaceRepository, tagRepository);

        // 3. Copia i task vecchi
        Set<Task> existingTasks = new HashSet<>(existingProject.getTasks());

        // 4. Mappa i nuovi task dal DTO
        Set<Task> updatedTasks = new HashSet<>();
        if (projectDTO.getTaskIds() != null) {

            for (Long taskId : projectDTO.getTaskIds()) {

                Task task = taskRepository.findById(taskId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

                task.setProject(existingProject); // Alla task setto il progetto
                updatedTasks.add(task); // Aggiungo al set di nuove task questa task
            }
        }
        
        // 5. Rimuovi i task non più presenti
        for (Task oldTask : existingTasks) {
            if (!updatedTasks.contains(oldTask)) {
                oldTask.setProject(null);
            }
        }

        // 6. Applica i nuovi task al progetto
        existingProject.setTasks(updatedTasks);

        // 7. Salva il progetto nel db
        projectRepository.save(existingProject);
    }

    // DELETE
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        projectRepository.deleteById(id);
    }
}