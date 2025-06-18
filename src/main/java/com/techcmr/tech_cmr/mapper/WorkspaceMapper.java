package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.WorkspaceDTO;
import com.techcmr.tech_cmr.model.Team;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Workspace;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper {

    @Mapping(source = "teams", target = "teamIds")
    @Mapping(source = "projects", target = "projectIds")
    WorkspaceDTO toDto(Workspace workspace);

    @Mapping(target = "teams", expression = "java(teamsFromIds(dto.getTeamIds()))")
    @Mapping(target = "projects", expression = "java(projectsFromIds(dto.getProjectIds()))")
    Workspace toEntity(WorkspaceDTO dto);

    default List<Long> teamsToIds(List<Team> teams) {
        if (teams == null) return null;
        return teams.stream()
                .map(Team::getId)
                .collect(Collectors.toList());
    }

    default List<Team> teamsFromIds(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream()
                .map(id -> {
                    Team team = new Team();
                    team.setId(id);
                    return team;
                })
                .collect(Collectors.toList());
    }

    default List<Long> projectsToIds(List<Project> projects) {
        if (projects == null) return null;
        return projects.stream()
                .map(Project::getId)
                .collect(Collectors.toList());
    }

    default List<Project> projectsFromIds(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream()
                .map(id -> {
                    Project project = new Project();
                    project.setId(id);
                    return project;
                })
                .collect(Collectors.toList());
    }
}