package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.ProjectDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Team;
import com.techcmr.tech_cmr.model.Workspace;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(source = "team.id", target = "teamId")
    @Mapping(source = "workspace.id", target = "workspaceId")
    @Mapping(source = "tags", target = "tagIds")
    ProjectDTO toDto(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "team", expression = "java(teamFromId(dto.getTeamId()))")
    @Mapping(target = "workspace", expression = "java(workspaceFromId(dto.getWorkspaceId()))")
    @Mapping(target = "tags", expression = "java(tagsFromIds(dto.getTagIds()))")
    Project toEntity(ProjectDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProjectDTO dto, @MappingTarget Project entity);

    // --- Default methods per conversioni personalizzate ---

    default Team teamFromId(Long id) {
        if (id == null) return null;
        Team team = new Team();
        team.setId(id);
        return team;
    }

    default Workspace workspaceFromId(Long id) {
        if (id == null) return null;
        Workspace workspace = new Workspace();
        workspace.setId(id);
        return workspace;
    }

    default List<Long> tagsToIds(List<Tag> tags) {
        if (tags == null) return null;
        return tags.stream()
                .map(Tag::getId)
                .collect(Collectors.toList());
    }

    default List<Tag> tagsFromIds(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream()
                .map(id -> {
                    Tag tag = new Tag();
                    tag.setId(id);
                    return tag;
                })
                .collect(Collectors.toList());
    }
}
