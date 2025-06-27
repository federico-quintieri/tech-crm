package com.techcmr.tech_cmr.mapper;

import com.techcmr.tech_cmr.dto.ProjectDTO;
import com.techcmr.tech_cmr.model.Attachment;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Section;
import com.techcmr.tech_cmr.model.Story;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.model.Team;
import com.techcmr.tech_cmr.model.Workspace;
import com.techcmr.tech_cmr.repository.AttachmentRepository;
import com.techcmr.tech_cmr.repository.SectionRepository;
import com.techcmr.tech_cmr.repository.StoryRepository;
import com.techcmr.tech_cmr.repository.TagRepository;
import com.techcmr.tech_cmr.repository.TaskRepository;
import com.techcmr.tech_cmr.repository.TeamRepository;
import com.techcmr.tech_cmr.repository.WorkspaceRepository;
import org.mapstruct.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    // DTO to Entity mapping
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "team", source = "teamId", qualifiedByName = "teamIdToTeam")
    @Mapping(target = "workspace", source = "workspaceId", qualifiedByName = "workspaceIdToWorkspace")
    @Mapping(target = "tags", source = "tagIds", qualifiedByName = "tagIdsToTags")
    @Mapping(target = "tasks", source = "taskIds", qualifiedByName = "taskIdsToTasks")
    @Mapping(target = "stories", source = "storyIds", qualifiedByName = "storyIdsToStories")
    @Mapping(target = "sections", source = "sectionIds", qualifiedByName = "sectionIdsToSections")
    @Mapping(target = "attachments", source = "attachmentIds", qualifiedByName = "attachmentIdsToAttachments")
    Project toEntity(ProjectDTO projectDTO,
            @Context TeamRepository teamRepository,
            @Context WorkspaceRepository workspaceRepository,
            @Context TaskRepository taskRepository,
            @Context TagRepository tagRepository,
            @Context SectionRepository sectionRepository,
            @Context AttachmentRepository attachmentRepository,
            @Context StoryRepository storyRepository);

    // Entity to DTO mapping
    @Mapping(target = "teamId", source = "team.id")
    @Mapping(target = "workspaceId", source = "workspace.id")
    @Mapping(target = "tagIds", source = "tags", qualifiedByName = "tagsToTagIds")
    @Mapping(target = "taskIds", source = "tasks", qualifiedByName = "tasksToTaskIds")
    @Mapping(target = "storyIds", source = "stories", qualifiedByName = "storiesToStoryIds")
    @Mapping(target = "attachmentIds", source = "attachments", qualifiedByName = "attachmentsToAttachmentIds")
    @Mapping(target = "sectionIds", source = "sections", qualifiedByName = "sectionsToSectionIds")
    ProjectDTO toDto(Project project);

    // Update existing entity from DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "team", source = "teamId", qualifiedByName = "teamIdToTeam")
    @Mapping(target = "workspace", source = "workspaceId", qualifiedByName = "workspaceIdToWorkspace")
    @Mapping(target = "tags", source = "tagIds", qualifiedByName = "tagIdsToTags")
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "stories", ignore = true)
    @Mapping(target = "sections", ignore = true)
    void updateEntityFromDto(ProjectDTO projectDTO,
            @MappingTarget Project project,
            @Context TeamRepository teamRepository,
            @Context WorkspaceRepository workspaceRepository,
            @Context TagRepository tagRepository);

    // MAPPING DA ID A OGGETTI
    @Named("teamIdToTeam")
    default Team teamIdToTeam(Long teamId, @Context TeamRepository teamRepository) {
        if (teamId == null) {
            return null;
        }
        return teamRepository.findById(teamId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found with id: " + teamId));
    }

    @Named("workspaceIdToWorkspace")
    default Workspace workspaceIdToWorkspace(Long workspaceId, @Context WorkspaceRepository workspaceRepository) {
        if (workspaceId == null) {
            return null;
        }
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Workspace not found with id: " + workspaceId));
    }

    @Named("tagIdsToTags")
    default Set<Tag> tagIdsToTags(Set<Long> tagIds, @Context TagRepository tagRepository) {
        if (tagIds == null || tagIds.isEmpty()) {
            return null;
        }
        return tagIds.stream()
                .map(id -> tagRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Tag not found with id: " + id)))
                .collect(Collectors.toSet());
    }

    @Named("taskIdsToTasks")
    default Set<Task> taskIdsToTasks(Set<Long> taskIds, @Context TaskRepository taskRepository) {
        if (taskIds == null || taskIds.isEmpty()) {
            return null;
        }
        return taskIds.stream()
                .map(id -> taskRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Task not found with id: " + id)))
                .collect(Collectors.toSet());
    }

    @Named("sectionIdsToSections")
    default Set<Section> sectionIdsToSections(Set<Long> sectionIds, @Context SectionRepository sectionRepository) {
        if (sectionIds == null || sectionIds.isEmpty()) {
            return null;
        }
        return sectionIds.stream()
                .map(id -> sectionRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Section not found with id: " + id)))
                .collect(Collectors.toSet());
    }

    @Named("attachmentIdsToAttachments")
    default Set<Attachment> attachmentIdsToAttachments(Set<Long> attachmentIds,
            @Context AttachmentRepository attachmentRepository) {
        if (attachmentIds == null || attachmentIds.isEmpty()) {
            return null;
        }
        return attachmentIds.stream()
                .map(id -> attachmentRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Attachment not found with id: " + id)))
                .collect(Collectors.toSet());
    }

    @Named("storyIdsToStories")
    default Set<Story> storyIdsToStories(Set<Long> storyIds, @Context StoryRepository storyRepository) {
        if (storyIds == null || storyIds.isEmpty()) {
            return null;
        }
        return storyIds.stream()
                .map(id -> storyRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Story not found with id: " + id)))
                .collect(Collectors.toSet());
    }

    // MAPPATURA DA OGGETTI A ID

    @Named("tagsToTagIds")
    default Set<Long> tagsToTagIds(Set<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        return tags.stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());
    }

    @Named("tasksToTaskIds")
    default Set<Long> tasksToTaskIds(Set<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return null;
        }
        return tasks.stream()
                .map(Task::getId)
                .collect(Collectors.toSet());
    }

    @Named("sectionsToSectionIds")
    default Set<Long> sectionsToSectionIds(Set<Section> sections) {
        if (sections == null || sections.isEmpty())
            return null;

        return sections.stream()
                .map(Section::getId)
                .collect(Collectors.toSet());
    }

    @Named("attachmentsToAttachmentIds")
    default Set<Long> attachmentsToAttachmentIds(Set<Attachment> attachments) {
        if (attachments == null || attachments.isEmpty())
            return null;

        return attachments.stream()
                .map(Attachment::getId)
                .collect(Collectors.toSet());

    }

    @Named("storiesToStoryIds")
    default Set<Long> storiesToStoryIds(Set<Story> stories) {
        if (stories == null || stories.isEmpty()) {
            return null;
        }
        return stories.stream()
                .map(Story::getId)
                .collect(Collectors.toSet());
    }

}