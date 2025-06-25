package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.TeamDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Team;
import com.techcmr.tech_cmr.model.Workspace;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import com.techcmr.tech_cmr.repository.WorkspaceRepository;
import org.mapstruct.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    // Entity -> DTO
    @Mapping(target = "projectIds", source = "projects", qualifiedByName = "projectsToProjectIds")
    @Mapping(target = "workspaceId", source = "workspace.id")
    TeamDTO toDto(Team team);

    // DTO -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "projects", source = "projectIds", qualifiedByName = "projectIdsToProjects")
    @Mapping(target = "workspace", source = "workspaceId", qualifiedByName = "workspaceIdToWorkspace")
    Team toEntity(TeamDTO dto,
                 @Context ProjectRepository projectRepository,
                 @Context WorkspaceRepository workspaceRepository);

    // Update Entity from DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "projects", source = "projectIds", qualifiedByName = "projectIdsToProjects")
    @Mapping(target = "workspace", source = "workspaceId", qualifiedByName = "workspaceIdToWorkspace")
    void updateEntityFromDto(TeamDTO dto,
                           @MappingTarget Team entity,
                           @Context ProjectRepository projectRepository,
                           @Context WorkspaceRepository workspaceRepository);

    // ===== Named Conversion Methods =====

    @Named("projectsToProjectIds")
    default List<Long> projectsToProjectIds(List<Project> projects) {
        if (projects == null) {
            return null;
        }
        return projects.stream()
                .map(Project::getId)
                .collect(Collectors.toList());
    }

    @Named("projectIdsToProjects")
    default List<Project> projectIdsToProjects(List<Long> projectIds,
                                             @Context ProjectRepository projectRepository) {
        if (projectIds == null) {
            return null;
        }
        return projectIds.stream()
                .map(id -> projectRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Project not found with id: " + id)))
                .collect(Collectors.toList());
    }

    @Named("workspaceIdToWorkspace")
    default Workspace workspaceIdToWorkspace(Long workspaceId,
                                           @Context WorkspaceRepository workspaceRepository) {
        if (workspaceId == null) {
            return null;
        }
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Workspace not found with id: " + workspaceId));
    }
}