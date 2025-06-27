package com.techcmr.tech_cmr.relations;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.techcmr.tech_cmr.dto.TaskDTO;
import com.techcmr.tech_cmr.model.Attachment;
import com.techcmr.tech_cmr.model.Story;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.repository.AttachmentRepository;
import com.techcmr.tech_cmr.repository.StoryRepository;

@Component
public class TaskRelationManager {

    private final AttachmentRepository attachmentRepository;
    private final StoryRepository storyRepository;

    public TaskRelationManager(AttachmentRepository attachmentRepository, StoryRepository storyRepository) {
        this.attachmentRepository = attachmentRepository;
        this.storyRepository = storyRepository;
    }

    // Metodo che aggiorna le stories collegate a task
    public void updateStoriesForTask(Task task, TaskDTO dto) {
        Set<Story> existingStories = new HashSet<>(task.getStories());
        Set<Story> updatedStories = new HashSet<>();

        if (dto.getStoryIds() != null) {
            for (Long storyId : dto.getStoryIds()) {
                Story story = storyRepository.findById(storyId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found"));
                story.setTask(task);
                updatedStories.add(story);
            }
        }
        for (Story oldStory : existingStories) {
            if (!updatedStories.contains(oldStory)) {
                oldStory.setTask(null);
            }
        }
        task.setStories(updatedStories);
    }

    // Metodo che aggiorna gli attachment collegati a task
    public void updateAttachmentsForTask(Task task, TaskDTO dto) {
        Set<Attachment> existingAttachments = new HashSet<>(task.getAttachments());
        Set<Attachment> updatedAttachments = new HashSet<>();

        if (dto.getAttachmentIds() != null) {
            for (Long attachmentId : dto.getAttachmentIds()) {
                Attachment attachment = attachmentRepository.findById(attachmentId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment not found"));
                attachment.setTask(task);
                updatedAttachments.add(attachment);
            }
        }
        for (Attachment oldAttachment : existingAttachments) {
            if (!updatedAttachments.contains(oldAttachment)) {
                oldAttachment.setTask(null);
            }
        }
        task.setAttachments(updatedAttachments);
    }

    // Metodo che aggiorna relazioni a chiamata post
    public void updateRelationsForPostTask(Task task) {
        // Attachments
        if (task.getAttachments() != null) {
            for (Attachment attachment : task.getAttachments()) {
                attachment.setTask(task);
            }
        }

        // Storie
        if (task.getStories() != null) {
            for (Story story : task.getStories()) {
                story.setTask(task);
            }
        }
    }

}
