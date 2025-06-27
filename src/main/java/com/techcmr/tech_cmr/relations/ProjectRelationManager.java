package com.techcmr.tech_cmr.relations;

import com.techcmr.tech_cmr.dto.ProjectDTO;
import com.techcmr.tech_cmr.model.Attachment;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Section;
import com.techcmr.tech_cmr.model.Story;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.repository.AttachmentRepository;
import com.techcmr.tech_cmr.repository.SectionRepository;
import com.techcmr.tech_cmr.repository.StoryRepository;
import com.techcmr.tech_cmr.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Component
public class ProjectRelationManager {

    // Repositiries
    private final TaskRepository taskRepository;
    private final AttachmentRepository attachmentRepository;
    private final SectionRepository sectionRepository;
    private final StoryRepository storyRepository;

    // Costruttore che inizializza
    public ProjectRelationManager(
            TaskRepository taskRepository,
            AttachmentRepository attachmentRepository,
            SectionRepository sectionRepository,
            StoryRepository storyRepository) {
        this.taskRepository = taskRepository;
        this.attachmentRepository = attachmentRepository;
        this.sectionRepository = sectionRepository;
        this.storyRepository = storyRepository;
    }

    // Metodo per aggiornare le relazioni Task con Project
    public void updateTasksForProject(Project project, ProjectDTO dto) {
        Set<Task> existingTasks = new HashSet<>(project.getTasks());
        Set<Task> updatedTasks = new HashSet<>();

        if (dto.getTaskIds() != null) {
            for (Long taskId : dto.getTaskIds()) {
                Task task = taskRepository.findById(taskId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
                task.setProject(project);
                updatedTasks.add(task);
            }
        }
        for (Task oldTask : existingTasks) {
            if (!updatedTasks.contains(oldTask)) {
                oldTask.setProject(null);
            }
        }
        project.setTasks(updatedTasks);
    }

    // Metodo per aggiornare le relazioni Attachment con Project
    public void updateAttachmentsForProject(Project project, ProjectDTO dto) {
        Set<Attachment> existingAttachments = new HashSet<>(project.getAttachments());
        Set<Attachment> updatedAttachments = new HashSet<>();

        if (dto.getAttachmentIds() != null) {
            for (Long attachmentId : dto.getAttachmentIds()) {
                Attachment attachment = attachmentRepository.findById(attachmentId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment not found"));
                attachment.setProject(project);
                updatedAttachments.add(attachment);
            }
        }
        for (Attachment oldAttachment : existingAttachments) {
            if (!updatedAttachments.contains(oldAttachment)) {
                oldAttachment.setProject(null);
            }
        }
        project.setAttachments(updatedAttachments);
    }

    // Metodo per aggiornare le relazioni Section con Project
    public void updateSectionsForProject(Project project, ProjectDTO dto) {
        Set<Section> existingSections = new HashSet<>(project.getSections());
        Set<Section> updatedSections = new HashSet<>();

        if (dto.getSectionIds() != null) {
            for (Long sectionId : dto.getSectionIds()) {
                Section section = sectionRepository.findById(sectionId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found"));
                section.setProject(project);
                updatedSections.add(section);
            }
        }
        for (Section oldSection : existingSections) {
            if (!updatedSections.contains(oldSection)) {
                oldSection.setProject(null);
            }
        }
        project.setSections(updatedSections);
    }

    // Metodo per aggiornare le relazioni Story con Project
    public void updateStoriesForProject(Project project, ProjectDTO dto) {
        Set<Story> existingStories = new HashSet<>(project.getStories());
        Set<Story> updatedStories = new HashSet<>();

        if (dto.getStoryIds() != null) {
            for (Long storyId : dto.getStoryIds()) {
                Story story = storyRepository.findById(storyId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found"));
                story.setProject(project);
                updatedStories.add(story);
            }
        }
        for (Story oldStory : existingStories) {
            if (!updatedStories.contains(oldStory)) {
                oldStory.setProject(null);
            }
        }
        project.setStories(updatedStories);
    }

    // Metodo per aggiornare le relazioni alla chiamata POST
    public void updateRelationsForPostProject(Project project) {
        // Set bidirectional relationship for tasks
        if (project.getTasks() != null) {
            for (Task task : project.getTasks()) {
                task.setProject(project);
            }
        }

        // Setta il progetto agli attachments
        if (project.getAttachments() != null) {
            for (Attachment attachment : project.getAttachments()) {
                attachment.setProject(project);
            }
        }

        // Setta il progetto alle sections
        if (project.getSections() != null) {
            for (Section section : project.getSections()) {
                section.setProject(project);
            }

        }

        // Setta il progetto alle storie
        if (project.getStories() != null) {
            for (Story story : project.getStories()) {
                story.setProject(project);
            }
        }
    }
}
