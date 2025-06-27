package com.techcmr.tech_cmr.relations;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.techcmr.tech_cmr.dto.TagDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import com.techcmr.tech_cmr.repository.TaskRepository;

@Component
public class TagRelationManager {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public TagRelationManager(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    // Aggiorna relazioni progetti a tags
    public void updatesProjectsForTag(Tag tag, TagDTO dto) {
        // Gestione manuale delle relazioni Task
        if (dto.getTaskIds() != null) {
            Set<Task> newTasks = new HashSet<>(taskRepository.findAllById(dto.getTaskIds()));
            Set<Task> oldTasks = tag.getTasks() != null ? new HashSet<>(tag.getTasks()) : new HashSet<>();

            for (Task task : newTasks) {
                task.getTags().add(tag);
                taskRepository.save(task);
            }

            for (Task task : oldTasks) {
                if (!newTasks.contains(task)) {
                    task.getTags().remove(tag);
                    taskRepository.save(task);
                }
            }

            tag.setTasks(newTasks);
        }
    }

    // Aggiorna relazioni tasks a tags
    public void updatesTasksForTag(Tag tag, TagDTO dto) {
        // Gestione manuale delle relazioni Project
        if (dto.getProjectIds() != null) {
            Set<Project> newProjects = new HashSet<>(projectRepository.findAllById(dto.getProjectIds()));
            Set<Project> oldProjects = tag.getProjects() != null ? new HashSet<>(tag.getProjects()) : new HashSet<>();

            for (Project project : newProjects) {
                project.getTags().add(tag);
                projectRepository.save(project);
            }

            for (Project project : oldProjects) {
                if (!newProjects.contains(project)) {
                    project.getTags().remove(tag);
                    projectRepository.save(project);
                }
            }

            tag.setProjects(newProjects);
        }
    }

    // Metodo che aggiorna relazioni per chiamata POST Tag
    public void updateRelationsForPostTag(Tag tag) {
        
        for (Task task : tag.getTasks()) {
            task.getTags().add(tag);
        }
        
        for (Project project : tag.getProjects()) {
            project.getTags().add(tag);
        }

    }

}
