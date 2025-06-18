package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.TagDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(source = "tasks", target = "taskIds")
    @Mapping(source = "projects", target = "projectIds")
    TagDTO toDto(Tag tag);

    @Mapping(target = "tasks", expression = "java(tasksFromIds(dto.getTaskIds()))")
    @Mapping(target = "projects", expression = "java(projectsFromIds(dto.getProjectIds()))")
    Tag toEntity(TagDTO dto);

    // Default methods per convertire liste entità <-> liste di id

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
                    Task t = new Task();
                    t.setId(id);
                    return t;
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
                    Project p = new Project();
                    p.setId(id);
                    return p;
                })
                .collect(Collectors.toList());
    }
}