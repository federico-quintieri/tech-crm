package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.TagDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import com.techcmr.tech_cmr.repository.TaskRepository;
import com.techcmr.tech_cmr.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(target = "taskIds", expression = "java(mapTasksToIds(tag.getTasks()))")
    @Mapping(target = "projectIds", expression = "java(mapProjectsToIds(tag.getProjects()))")
    TagDTO toDto(Tag tag);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", expression = "java(mapIdsToTasks(dto.getTaskIds(), taskRepository))")
    @Mapping(target = "projects", expression = "java(mapIdsToProjects(dto.getProjectIds(), projectRepository))")
    Tag toEntity(TagDTO dto, @Context ProjectRepository projectRepository, @Context TaskRepository taskRepository);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "projects", ignore = true)
    void updateEntityFromDto(TagDTO dto, @MappingTarget Tag entity);

    // Metodo che converte il set di task in set di id delle tast
    default Set<Long> mapTasksToIds(Set<Task> tasks) {
        if (tasks == null) return null;
        return tasks.stream().map(Task::getId).collect(Collectors.toSet());
    }

    // Metodo che converte il set di project in set di id dei project
    default Set<Long> mapProjectsToIds(Set<Project> projects) {
        if (projects == null) return null;
        return projects.stream().map(Project::getId).collect(Collectors.toSet());
    }

    // MANY TO MANY CON PROJECTS E TASKS

    // Metodo che prende gli ids e mi ci fa un set di tasks
    default Set<Task> mapIdsToTasks(Set<Long> ids, @Context TaskRepository taskRepository) {
        if (ids == null) return null;
        return ids.stream()
                .map(id -> taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found: " + id)))
                .collect(Collectors.toSet());
    }

    // Metodo che prende gli ids e mi ci fa un set di projects
    default Set<Project> mapIdsToProjects(Set<Long> ids, @Context ProjectRepository projectRepository) {
        if (ids == null) return null;
        return ids.stream()
                .map(id -> projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project not found: " + id)))
                .collect(Collectors.toSet());
    }

}

