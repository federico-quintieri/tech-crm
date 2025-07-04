package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.StoryDTO;
import com.techcmr.tech_cmr.model.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StoryMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "project.id", target = "projectId")
    StoryDTO toDto(Story story);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", expression = "java(userFromId(dto.getAuthorId()))")
    @Mapping(target = "task", expression = "java(taskFromId(dto.getTaskId()))")
    @Mapping(target = "project", expression = "java(projectFromId(dto.getProjectId()))")
    Story toEntity(StoryDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "author", expression = "java(userFromId(dto.getAuthorId()))")
    @Mapping(target = "task", expression = "java(taskFromId(dto.getTaskId()))")
    @Mapping(target = "project", expression = "java(projectFromId(dto.getProjectId()))")
    void updateEntityFromDto(StoryDTO dto, @MappingTarget Story entity);

    // Metodi di utilità per creare riferimenti con solo ID

    default User userFromId(Long id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }

    default Task taskFromId(Long id) {
        if (id == null) return null;
        Task task = new Task();
        task.setId(id);
        return task;
    }

    default Project projectFromId(Long id) {
        if (id == null) return null;
        Project project = new Project();
        project.setId(id);
        return project;
    }
}