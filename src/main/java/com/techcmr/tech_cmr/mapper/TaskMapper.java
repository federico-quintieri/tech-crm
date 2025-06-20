package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.ProjectDTO;
import com.techcmr.tech_cmr.dto.TaskDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.service.TagService;
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


    // Metodo che converte il Set di tags in set di id
    default Set<Long> mapTagsToIds(Set<Tag> tags) {
        if (tags == null) return null;
        return tags.stream().map(Tag::getId).collect(Collectors.toSet());
    }

    // Metodo che converte il Set di tags id in set di tags
    default Set<Tag> mapIdsToTags(Set<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> {
            Tag tag = new Tag();
            tag.setId(id);
            return tag;
        }).collect(Collectors.toSet());
    }

    // Metodo richiamato in automatico dopo il mapping TaskDTO a Task
    // Converte gli id dei tag in veri oggetti tag nella entity Task
    @AfterMapping
    default void mapTags(TaskDTO dto, @MappingTarget Task entity, TagService tagService) {
        if (dto.getTagIds() != null) {
            Set<Tag> tags = dto.getTagIds().stream()
                    .map(id -> tagService.findById(id)
                            .orElseThrow(() -> new RuntimeException("Tag " + id + " not found")))
                    .collect(Collectors.toSet());
            entity.setTags(tags);
        }
    }
}
