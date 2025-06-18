package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.TaskDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Section;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "section.id", target = "sectionId")
    @Mapping(source = "tags", target = "tagIds")
    TaskDTO toDto(Task task);

    @Mapping(target = "project", expression = "java(projectFromId(dto.getProjectId()))")
    @Mapping(target = "section", expression = "java(sectionFromId(dto.getSectionId()))")
    @Mapping(target = "tags", expression = "java(tagsFromIds(dto.getTagIds()))")
    Task toEntity(TaskDTO dto);

    // Metodi helper per convertire liste di entità e id

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

    default Project projectFromId(Long id) {
        if (id == null) return null;
        Project p = new Project();
        p.setId(id);
        return p;
    }

    default Section sectionFromId(Long id) {
        if (id == null) return null;
        Section s = new Section();
        s.setId(id);
        return s;
    }
}
