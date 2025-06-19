package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.TaskDTO;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Task;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TaskMapper {


    @Mapping(target = "tagIds", expression = "java(mapTagsToIds(task.getTags()))")
    TaskDTO toDto(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", expression = "java(mapIdsToTags(dto.getTagIds()))")
    Task toEntity(TaskDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(TaskDTO dto, @MappingTarget Task entity);

    // Helpers

    default Set<Long> mapTagsToIds(Set<Tag> tags) {
        if (tags == null) return null;
        return tags.stream().map(Tag::getId).collect(Collectors.toSet());
    }

    default Set<Tag> mapIdsToTags(Set<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> {
            Tag tag = new Tag();
            tag.setId(id);
            return tag;
        }).collect(Collectors.toSet());
    }
}
