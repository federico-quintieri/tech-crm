package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.TagDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Task;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(target = "taskIds", expression = "java(mapTasksToIds(tag.getTasks()))")
    @Mapping(target = "projectIds", expression = "java(mapProjectsToIds(tag.getProjects()))")
    TagDTO toDto(Tag tag);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", expression = "java(mapIdsToTasks(dto.getTaskIds()))")
    @Mapping(target = "projects", expression = "java(mapIdsToProjects(dto.getProjectIds()))")
    Tag toEntity(TagDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(TagDTO dto, @MappingTarget Tag entity);

    // Helpers

    default Set<Long> mapTasksToIds(Set<Task> tasks) {
        if (tasks == null) return null;
        return tasks.stream().map(Task::getId).collect(Collectors.toSet());
    }

    default Set<Task> mapIdsToTasks(Set<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> {
            Task t = new Task();
            t.setId(id);
            return t;
        }).collect(Collectors.toSet());
    }

    default Set<Long> mapProjectsToIds(Set<Project> projects) {
        if (projects == null) return null;
        return projects.stream().map(Project::getId).collect(Collectors.toSet());
    }

    default Set<Project> mapIdsToProjects(Set<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> {
            Project p = new Project();
            p.setId(id);
            return p;
        }).collect(Collectors.toSet());
    }
}
