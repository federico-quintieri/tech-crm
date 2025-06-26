package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.ProjectDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.model.Team;
import com.techcmr.tech_cmr.model.Workspace;
import com.techcmr.tech_cmr.repository.TagRepository;
import com.techcmr.tech_cmr.repository.TaskRepository;
import com.techcmr.tech_cmr.repository.TeamRepository;
import com.techcmr.tech_cmr.repository.WorkspaceRepository;
import org.mapstruct.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    // DTO to Entity mapping
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "team", source = "teamId", qualifiedByName = "teamIdToTeam")
    @Mapping(target = "workspace", source = "workspaceId", qualifiedByName = "workspaceIdToWorkspace")
    @Mapping(target = "tags", source = "tagIds", qualifiedByName = "tagIdsToTags")
    @Mapping(target = "tasks", source = "taskIds", qualifiedByName = "taskIdsToTasks")
    Project toEntity(ProjectDTO projectDTO, 
                     @Context TeamRepository teamRepository, 
                     @Context WorkspaceRepository workspaceRepository, 
                     @Context TaskRepository taskRepository,
                     @Context TagRepository tagRepository);

    // Entity to DTO mapping
    @Mapping(target = "teamId", source = "team.id")
    @Mapping(target = "workspaceId", source = "workspace.id")
    @Mapping(target = "tagIds", source = "tags", qualifiedByName = "tagsToTagIds")
    @Mapping(target = "taskIds", source = "tasks", qualifiedByName = "tasksToTaskIds")
    ProjectDTO toDto(Project project);

    // Update existing entity from DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "team", source = "teamId", qualifiedByName = "teamIdToTeam")
    @Mapping(target = "workspace", source = "workspaceId", qualifiedByName = "workspaceIdToWorkspace")
    @Mapping(target = "tags", source = "tagIds", qualifiedByName = "tagIdsToTags")
    @Mapping(target = "tasks", ignore = true)
    void updateEntityFromDto(ProjectDTO projectDTO, 
                           @MappingTarget Project project, 
                           @Context TeamRepository teamRepository, 
                           @Context WorkspaceRepository workspaceRepository,  
                           @Context TagRepository tagRepository);

    // Custom mapping methods using repositories
    @Named("teamIdToTeam")
    default Team teamIdToTeam(Long teamId, @Context TeamRepository teamRepository) {
        if (teamId == null) {
            return null;
        }
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found with id: " + teamId));
    }

    @Named("workspaceIdToWorkspace")
    default Workspace workspaceIdToWorkspace(Long workspaceId, @Context WorkspaceRepository workspaceRepository) {
        if (workspaceId == null) {
            return null;
        }
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace not found with id: " + workspaceId));
    }

    @Named("tagIdsToTags")
    default Set<Tag> tagIdsToTags(Set<Long> tagIds, @Context TagRepository tagRepository) {
        if (tagIds == null || tagIds.isEmpty()) {
            return null;
        }
        return tagIds.stream()
                .map(id -> tagRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found with id: " + id)))
                .collect(Collectors.toSet());
    }

    @Named("taskIdsToTasks")
    default Set<Task> taskIdsToTasks(Set<Long> taskIds, @Context TaskRepository taskRepository) {
        if (taskIds == null || taskIds.isEmpty()) {
            return null;
        }
        return taskIds.stream()
                .map(id -> taskRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + id)))
                .collect(Collectors.toSet());
    }

    @Named("tagsToTagIds")
    default Set<Long> tagsToTagIds(Set<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        return tags.stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());
    }

    @Named("tasksToTaskIds")
    default Set<Long> tasksToTaskIds(Set<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return null;
        }
        return tasks.stream()
                .map(Task::getId)
                .collect(Collectors.toSet());
    }
}