package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.TagDTO;
import com.techcmr.tech_cmr.dto.TeamDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Team;
import com.techcmr.tech_cmr.model.Workspace;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    @Mapping(source = "projects", target = "projectIds")
    @Mapping(source = "workspace.id", target = "workspaceId")
    TeamDTO toDto(Team team);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "projects", expression = "java(projectsFromIds(dto.getProjectIds()))")
    @Mapping(target = "workspace", expression = "java(workspaceFromId(dto.getWorkspaceId()))")
    Team toEntity(TeamDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(TeamDTO team, @MappingTarget Team entity);

    // Helper per conversione lista Project -> lista ID
    default List<Long> projectsToIds(List<Project> projects) {
        if (projects == null) return null;
        return projects.stream()
                .map(Project::getId)
                .collect(Collectors.toList());
    }

    // Helper per conversione lista ID -> lista Project (solo id settato)
    default List<Project> projectsFromIds(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream()
                .map(id -> {
                    Project p = new Project();
                    p.setId(id);
                    return p;
                })
                .collect(Collectors.toList());
    }

    // Helper per Workspace da id
    default Workspace workspaceFromId(Long id) {
        if (id == null) return null;
        Workspace w = new Workspace();
        w.setId(id);
        return w;
    }
}