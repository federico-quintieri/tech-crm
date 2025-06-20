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
    @Mapping(target = "tasks", ignore = true)  // Ignora qui!
    @Mapping(target = "projects", ignore = true)  // Ignora qui!
    Tag toEntity(TagDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "projects", ignore = true)
    void updateEntityFromDto(TagDTO dto, @MappingTarget Tag entity);

    // Metodo che converte il set di task in set di id delle tast
    default Set<Long> mapTasksToIds(Set<Task> tasks) {
        if (tasks == null) return null;
        return tasks.stream().map(Task::getId).collect(Collectors.toSet());
    }

    // Metodo che converte il set di project in set di id dei project
    default Set<Long> mapProjectsToIds(Set<Project> projects) {
        if (projects == null) return null;
        return projects.stream().map(Project::getId).collect(Collectors.toSet());
    }
}
