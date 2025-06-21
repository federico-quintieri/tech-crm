package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.ProjectDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Team;
import com.techcmr.tech_cmr.model.Workspace;
import com.techcmr.tech_cmr.service.TagService;
import com.techcmr.tech_cmr.service.TeamService;
import com.techcmr.tech_cmr.service.WorkspaceService;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

// Definisco interfaccia con il mapper che usa i service delle relazioni
@Mapper(componentModel = "spring", uses = {TeamService.class, WorkspaceService.class, TagService.class})
public interface ProjectMapper {

    // Definisco quali metodi devono essere usati per convertire dei campi da entity a dto
    @Mapping(target = "tagIds", expression = "java(mapTagsToIds(project.getTags()))")
    @Mapping(target = "teamId", expression = "java(mapTeamToId(project.getTeam()))")
    @Mapping(target = "workspaceId", expression = "java(mapWorkspaceToId(project.getWorkspace()))")
    ProjectDTO toDto(Project project);

    // Definisco quali metodi devono essere usati per convertire dei campi da dto a entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", expression = "java(mapIdsToTags(dto.getTagIds()))")
    @Mapping(target = "team", expression = "java(mapIdToTeam(dto.getTeamId(), teamService))")
    @Mapping(target = "workspace", expression = "java(mapIdToWorkspace(dto.getWorkspaceId(), workspaceService))")
    Project toEntity(ProjectDTO dto, @Context TeamService teamService, @Context WorkspaceService workspaceService);

    // Definisco quali metodi devono essere usati per mergiare entity da dto
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tags", expression = "java(mapIdsToTags(dto.getTagIds()))")
    @Mapping(target = "team", expression = "java(mapIdToTeam(dto.getTeamId(), teamService))")
    @Mapping(target = "workspace", expression = "java(mapIdToWorkspace(dto.getWorkspaceId(), workspaceService))")
    void updateEntityFromDto(ProjectDTO dto, @MappingTarget Project entity, @Context TeamService teamService, @Context WorkspaceService workspaceService);

    // METODI CHE PRENDONO OGGETTO/i E RESTITUISCONO ID/s

    // Prende i tag e mi fa un set dei loro id
    default Set<Long> mapTagsToIds(Set<Tag> tags) {
        if (tags == null) return null;
        return tags.stream().map(Tag::getId).collect(Collectors.toSet());
    }

    // Da team mi restituisce l'id
    default Long mapTeamToId(Team team) {
        return team != null ? team.getId() : null;
    }

    // Da workspace mi restituisce l'id
    default Long mapWorkspaceToId(Workspace workspace) {
        return workspace != null ? workspace.getId() : null;
    }

    // METODI CHE PRENDONO ID/s E RESTITUISCONO OGGETTO/i

    // Metodo che prende gli ids e mi ci fa un set di tags
    default Set<Tag> mapIdsToTags(Set<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> {
            Tag tag = new Tag();
            tag.setId(id);
            return tag;
        }).collect(Collectors.toSet());
    }

    // Metodo che da id team mi prende il team
    default Team mapIdToTeam(Long id, TeamService teamService) {
        if (id == null) return null;
        return teamService.findById(id)
                .orElseThrow(() -> new RuntimeException("Team with id " + id + " not found"));
    }

    // Metodo che da id di workspace mi prende il workspace
    default Workspace mapIdToWorkspace(Long id, WorkspaceService workspaceService) {
        if (id == null) return null;
        return workspaceService.findById(id)
                .orElseThrow(() -> new RuntimeException("Workspace with id " + id + " not found"));
    }




}
