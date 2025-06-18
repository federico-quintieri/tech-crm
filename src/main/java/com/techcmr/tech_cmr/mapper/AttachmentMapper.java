package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.AttachmentDTO;
import com.techcmr.tech_cmr.model.Attachment;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "project.id", target = "projectId")
    AttachmentDTO toDto(Attachment attachment);

    @Mapping(target = "task", expression = "java(taskFromId(dto.getTaskId()))")
    @Mapping(target = "project", expression = "java(projectFromId(dto.getProjectId()))")
    Attachment toEntity(AttachmentDTO dto);

    default Task taskFromId(Long id) {
        if (id == null) {
            return null;
        }
        Task task = new Task();
        task.setId(id);
        return task;
    }

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}