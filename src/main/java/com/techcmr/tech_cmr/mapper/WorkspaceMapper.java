package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.WorkspaceDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Team;
import com.techcmr.tech_cmr.model.Workspace;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import com.techcmr.tech_cmr.repository.TeamRepository;
import org.mapstruct.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper {

    // Entity -> DTO
    @Mapping(target = "teamIds", source = "teams", qualifiedByName = "teamsToTeamIds")
    @Mapping(target = "projectIds", source = "projects", qualifiedByName = "projectsToProjectIds")
    WorkspaceDTO toDto(Workspace workspace);

    // DTO -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teams", source = "teamIds", qualifiedByName = "teamIdsToTeams")
    @Mapping(target = "projects", source = "projectIds", qualifiedByName = "projectIdsToProjects")
    Workspace toEntity(WorkspaceDTO dto,
                      @Context TeamRepository teamRepository,
                      @Context ProjectRepository projectRepository);

    // Update Entity from DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teams", source = "teamIds", qualifiedByName = "teamIdsToTeams")
    @Mapping(target = "projects", source = "projectIds", qualifiedByName = "projectIdsToProjects")
    void updateEntityFromDto(WorkspaceDTO dto,
                           @MappingTarget Workspace entity,
                           @Context TeamRepository teamRepository,
                           @Context ProjectRepository projectRepository);

    // ===== Named Conversion Methods =====

    @Named("teamsToTeamIds")
    default List<Long> teamsToTeamIds(List<Team> teams) {
        if (teams == null) {
            return null;
        }
        return teams.stream()
                .map(Team::getId)
                .collect(Collectors.toList());
    }

    @Named("teamIdsToTeams")
    default List<Team> teamIdsToTeams(List<Long> teamIds,
                                    @Context TeamRepository teamRepository) {
        if (teamIds == null) {
            return null;
        }
        return teamIds.stream()
                .map(id -> teamRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Team not found with id: " + id)))
                .peek(team -> team.setWorkspace(new Workspace())) // Workspace temporaneo
                .collect(Collectors.toList());
    }

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
                .peek(project -> project.setWorkspace(new Workspace())) // Workspace temporaneo
                .collect(Collectors.toList());
    }
}