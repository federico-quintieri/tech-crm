package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.SectionDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Section;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.service.ProjectService;
import com.techcmr.tech_cmr.service.TaskService;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

// Definisco interfaccia con il mapper che usa i service delle relazioni
@Mapper(componentModel = "spring", uses = {ProjectService.class, TaskService.class})
public interface SectionMapper {

    // Definisco quali metodi devono essere usati per convertire dei campi da entity a dto
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(target = "taskIds", expression = "java(tasksToIds(section.getTasks()))")
    SectionDTO toDto(Section section);

    // Definisco quali metodi devono essere usati per convertire dei campi da dto a entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", expression = "java(projectFromId(dto.getProjectId(), projectService))")
    @Mapping(target = "tasks", expression = "java(tasksFromIds(dto.getTaskIds(), taskService))")
    Section toEntity(SectionDTO dto, @Context ProjectService projectService, @Context TaskService taskService);

    // Definisco quali metodi devono essere usati per mergiare entity a dto
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "project", expression = "java(projectFromId(dto.getProjectId(), projectService))")
    @Mapping(target = "tasks", expression = "java(tasksFromIds(dto.getTaskIds(), taskService))")
    void updateEntityFromDto(SectionDTO dto, @MappingTarget Section entity, @Context ProjectService projectService, @Context TaskService taskService);


    // METODI che da oggetto/i mi danno id/s

    default List<Long> tasksToIds(List<Task> tasks) {
        if (tasks == null) return null;
        return tasks.stream().map(Task::getId).collect(Collectors.toList());
    }

    // Metodi che da Id/s mi danno oggetto/i

    default Project projectFromId(Long id, ProjectService projectService) {
        if (id == null) return null;
        return projectService.findById(id)
                .orElseThrow(() -> new RuntimeException("Project with id " + id + " not found"));
    }

    default List<Task> tasksFromIds(List<Long> ids, TaskService taskService) {
        if (ids == null) return null;
        return ids.stream()
                .map(id -> taskService.findById(id)
                        .orElseThrow(() -> new RuntimeException("Task with id " + id + " not found")))
                .collect(Collectors.toList());
    }

}
