package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.ProjectDTO;
import com.techcmr.tech_cmr.mapper.ProjectMapper;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectService {

    // Autowired repository
    @Autowired
    private ProjectRepository projectRepository;

    // Autowired services
    @Autowired
    private TeamService teamService;
    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private TaskService taskService;

    // Autowired mapper
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
        Project project = projectRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return projectMapper.toDto(project);
    }

    // CREATE
    public ProjectDTO createProject(ProjectDTO projectDTO) {

        // Mappatura completa con i servizi passati come @Context
        Project project = projectMapper.toEntity(projectDTO, teamService, workspaceService, taskService);

        // Qui in pratica vado per ogni task ad assegnargli il progetto
        if(project.getTasks() != null){
            for(Task task : project.getTasks()){
                task.setProject(project);
            }
        }

        // Salvataggio
        Project savedProject = projectRepository.save(project);

        // Ritorno DTO
        return projectMapper.toDto(savedProject);

    }

    // UPDATE
    public void updateProject(Long id, ProjectDTO projectDTO) {
        // 1. Trovo l'entità esistente
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 2. Applico i nuovi valori (merge tra DTO e entity esistente)
        projectMapper.updateEntityFromDto(projectDTO, existingProject, teamService, workspaceService,taskService);

        // Qui in pratica vado per ogni task ad assegnargli il progetto
        if(existingProject.getTasks() != null){
            for(Task task : existingProject.getTasks()){
                task.setProject(existingProject);
            }
        }
        // 3. Salvo
        projectRepository.save(existingProject);

    }

    // DELETE
    public void deleteProject(Long id) {
        projectRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        projectRepository.deleteById(id);
    }

}
