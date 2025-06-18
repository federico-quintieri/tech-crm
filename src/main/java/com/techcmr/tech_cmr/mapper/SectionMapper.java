package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.SectionDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Section;
import com.techcmr.tech_cmr.model.Task;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SectionMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "tasks", target = "taskIds")
    SectionDTO toDto(Section section);

    @Mapping(target = "project", expression = "java(projectFromId(dto.getProjectId()))")
    @Mapping(target = "tasks", expression = "java(tasksFromIds(dto.getTaskIds()))")
    Section toEntity(SectionDTO dto);

    // --- Default methods ---

    default Project projectFromId(Long id) {
        if (id == null) return null;
        Project project = new Project();
        project.setId(id);
        return project;
    }

    default List<Long> tasksToIds(List<Task> tasks) {
        if (tasks == null) return null;
        return tasks.stream()
                .map(Task::getId)
                .collect(Collectors.toList());
    }

    default List<Task> tasksFromIds(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream()
                .map(id -> {
                    Task task = new Task();
                    task.setId(id);
                    return task;
                })
                .collect(Collectors.toList());
    }
}
