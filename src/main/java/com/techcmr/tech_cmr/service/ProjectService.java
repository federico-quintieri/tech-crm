package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.ProjectDTO;
import com.techcmr.tech_cmr.mapper.ProjectMapper;
import com.techcmr.tech_cmr.model.Attachment;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Section;
import com.techcmr.tech_cmr.model.Story;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.relations.ProjectRelationManager;
import com.techcmr.tech_cmr.relations.RelationManager;
import com.techcmr.tech_cmr.repository.AttachmentRepository;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import com.techcmr.tech_cmr.repository.SectionRepository;
import com.techcmr.tech_cmr.repository.StoryRepository;
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
    private SectionRepository sectionRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectRelationManager projectRelationManager;

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
                taskRepository, tagRepository, sectionRepository, attachmentRepository, storyRepository);

        projectRelationManager.updateRelationsForPostProject(project);

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

        // 3. Metodi per aggiornare le relazioni
        projectRelationManager.updateTasksForProject(existingProject, projectDTO);
        projectRelationManager.updateAttachmentsForProject(existingProject, projectDTO);
        projectRelationManager.updateStoriesForProject(existingProject, projectDTO);
        projectRelationManager.updateSectionsForProject(existingProject, projectDTO);

        // 4. Salva il progetto nel db
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