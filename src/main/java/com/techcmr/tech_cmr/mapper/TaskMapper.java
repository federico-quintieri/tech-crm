package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.TaskDTO;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Section;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.repository.AttachmentRepository;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import com.techcmr.tech_cmr.repository.SectionRepository;
import com.techcmr.tech_cmr.repository.StoryRepository;
import com.techcmr.tech_cmr.repository.TagRepository;
import org.mapstruct.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    // DTO to Entity mapping
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "project", source = "projectId", qualifiedByName = "projectIdToProject")
    @Mapping(target = "section", source = "sectionId", qualifiedByName = "sectionIdToSection")
    @Mapping(target = "attachments", source = "attachmentIds", qualifiedByName = "attachmentIdsToAttachments")
    @Mapping(target = "stories", source = "storyIds", qualifiedByName = "storyIdsToStories")
    @Mapping(target = "tags", source = "tagIds", qualifiedByName = "tagIdsToTags")
    Task toEntity(TaskDTO dto,
            @Context TagRepository tagRepository,
            @Context ProjectRepository projectRepository,
            @Context SectionRepository sectionRepository,
            @Context StoryRepository storyRepository,
            @Context AttachmentRepository attachmentRepository);

    // Entity to DTO mapping
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "sectionId", source = "section.id")
    @Mapping(target = "tagIds", source = "tags", qualifiedByName = "tagsToTagIds")
    @Mapping(target = "attachmentIds", source = "attachments", qualifiedByName = "attachmentsToAttachmentIds")
    @Mapping(target = "storyIds", source = "stories", qualifiedByName = "storiesToStoriesIds")
    TaskDTO toDto(Task task);

    // Update existing entity from DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "project", source = "projectId", qualifiedByName = "projectIdToProject")
    @Mapping(target = "section", source = "sectionId", qualifiedByName = "sectionIdToSection")
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "stories", ignore = true)
    @Mapping(target = "tags", source = "tagIds", qualifiedByName = "tagIdsToTags")
    void updateEntityFromDto(TaskDTO dto,
            @MappingTarget Task entity,
            @Context ProjectRepository projectRepository,
            @Context SectionRepository sectionRepository,
            @Context TagRepository tagRepository);

    // ===== Custom Mapping Methods (Using @Named for clarity) =====

    @Named("attachmentIdsToAttachments")
    default Set<com.techcmr.tech_cmr.model.Attachment> attachmentIdsToAttachments(Set<Long> ids,
            @Context AttachmentRepository repo) {
        if (ids == null || ids.isEmpty())
            return null;
        return ids.stream()
                .map(id -> repo.findById(id).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment not found with id: " + id)))
                .collect(Collectors.toSet());
    }

    @Named("storyIdsToStories")
    default Set<com.techcmr.tech_cmr.model.Story> storyIdsToStories(Set<Long> ids, @Context StoryRepository repo) {
        if (ids == null || ids.isEmpty())
            return null;
        return ids.stream()
                .map(id -> repo.findById(id).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found with id: " + id)))
                .collect(Collectors.toSet());
    }

    @Named("projectIdToProject")
    default Project projectIdToProject(Long projectId, @Context ProjectRepository projectRepository) {
        if (projectId == null) {
            return null;
        }
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found with id: " + projectId));
    }

    @Named("sectionIdToSection")
    default Section sectionIdToSection(Long sectionId, @Context SectionRepository sectionRepository) {
        if (sectionId == null) {
            return null;
        }
        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Section not found with id: " + sectionId));
    }

    @Named("tagIdsToTags")
    default Set<Tag> tagIdsToTags(Set<Long> tagIds, @Context TagRepository tagRepository) {
        if (tagIds == null || tagIds.isEmpty()) {
            return null;
        }
        return tagIds.stream()
                .map(id -> tagRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Tag not found with id: " + id)))
                .collect(Collectors.toSet());
    }

    @Named("tagsToTagIds")
    default Set<Long> tagsToTagIds(Set<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        return tags.stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());
    }

    @Named("attachmentsToAttachmentIds")
    default Set<Long> attachmentsToAttachmentIds(Set<com.techcmr.tech_cmr.model.Attachment> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return null;
        }
        return attachments.stream()
                .map(com.techcmr.tech_cmr.model.Attachment::getId)
                .collect(Collectors.toSet());
    }

    @Named("storiesToStoriesIds")
    default Set<Long> storiesToStoriesIds(Set<com.techcmr.tech_cmr.model.Story> stories) {
        if (stories == null || stories.isEmpty()) {
            return null;
        }
        return stories.stream()
                .map(com.techcmr.tech_cmr.model.Story::getId)
                .collect(Collectors.toSet());
    }

}