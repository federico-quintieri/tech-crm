package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.ProjectDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.service.TagService;
import org.aspectj.lang.annotation.After;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "tagIds", expression = "java(mapTagsToIds(project.getTags()))")
    ProjectDTO toDto(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", expression = "java(mapIdsToTags(dto.getTagIds()))")
    Project toEntity(ProjectDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tags", ignore = true)
    void updateEntityFromDto(ProjectDTO dto, @MappingTarget Project entity);

    // Metodo che prende i tag e mi fa un set dei loro id
    default Set<Long> mapTagsToIds(Set<Tag> tags) {
        if (tags == null) return null;
        return tags.stream().map(Tag::getId).collect(Collectors.toSet());
    }

    // Metodo che prende gli ids e mi ci fa un set di tags
    default Set<Tag> mapIdsToTags(Set<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> {
            Tag tag = new Tag();
            tag.setId(id);
            return tag;
        }).collect(Collectors.toSet());
    }


    // Metodo richiamato in automatico dopo il mapping ProjectDTO a Project
    // Converte gli id dei tag in veri oggetti tag nella entity Project
    @AfterMapping
    default void mapTags(ProjectDTO dto, @MappingTarget Project entity, TagService tagService) {
        if (dto.getTagIds() != null) {
            Set<Tag> tags = dto.getTagIds().stream()
                    .map(id -> tagService.findById(id)
                            .orElseThrow(() -> new RuntimeException("Tag " + id + " not found")))
                    .collect(Collectors.toSet());
            entity.setTags(tags);
        }
    }
}
