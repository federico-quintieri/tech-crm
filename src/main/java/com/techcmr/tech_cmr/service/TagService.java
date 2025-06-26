package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.TagDTO;
import com.techcmr.tech_cmr.mapper.TagMapper;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import com.techcmr.tech_cmr.repository.TagRepository;
import com.techcmr.tech_cmr.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TagMapper tagMapper;

    public Optional<Tag> findById(Long id) {
        return tagRepository.findById(id);
    }

    // READ
    public List<TagDTO> findAllTags() {
        return tagRepository.findAll().stream().map(tagMapper::toDto).toList();
    }

    // SHOW
    public TagDTO findTagById(Long id) {
        Tag tag = tagRepository.findWithTasksAndProjectsById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found"));
        return tagMapper.toDto(tag);
    }

    // CREATE
    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = tagMapper.toEntity(tagDTO, projectRepository, taskRepository);

        for (Task task : tag.getTasks()) {
            task.getTags().add(tag);
        }
        for (Project project : tag.getProjects()) {
            project.getTags().add(tag);
        }

        // Salva di nuovo il tag aggiornato con relazioni
        tag = tagRepository.save(tag);

        return tagMapper.toDto(tag);
    }

    // UPDATE
    public void updateTag(Long id, TagDTO tagDTO) {
        Tag tag = tagRepository.findWithTasksAndProjectsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));

        // Aggiorna solo i campi semplici (es. name)
        tagMapper.updateEntityFromDto(tagDTO, tag);

        // Gestione manuale delle relazioni Task
        if (tagDTO.getTaskIds() != null) {
            Set<Task> newTasks = new HashSet<>(taskRepository.findAllById(tagDTO.getTaskIds()));
            Set<Task> oldTasks = tag.getTasks() != null ? new HashSet<>(tag.getTasks()) : new HashSet<>();

            for (Task task : newTasks) {
                task.getTags().add(tag);
                taskRepository.save(task);
            }

            for (Task task : oldTasks) {
                if (!newTasks.contains(task)) {
                    task.getTags().remove(tag);
                    taskRepository.save(task);
                }
            }

            tag.setTasks(newTasks);
        }

        // Gestione manuale delle relazioni Project
        if (tagDTO.getProjectIds() != null) {
            Set<Project> newProjects = new HashSet<>(projectRepository.findAllById(tagDTO.getProjectIds()));
            Set<Project> oldProjects = tag.getProjects() != null ? new HashSet<>(tag.getProjects()) : new HashSet<>();

            for (Project project : newProjects) {
                project.getTags().add(tag);
                projectRepository.save(project);
            }

            for (Project project : oldProjects) {
                if (!newProjects.contains(project)) {
                    project.getTags().remove(tag);
                    projectRepository.save(project);
                }
            }

            tag.setProjects(newProjects);
        }

        tagRepository.save(tag);
    }

    // DELETE
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findWithTasksAndProjectsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));

        // Rimuovi la relazione da entrambi i lati, se necessario
        if (tag.getTasks() != null) {
            for (Task task : tag.getTasks()) {
                task.getTags().remove(tag); // lato inverso
            }
            tag.getTasks().clear(); // lato diretto
        }

        if (tag.getProjects() != null) {
            for (Project project : tag.getProjects()) {
                project.getTags().remove(tag); // lato inverso
            }
            tag.getProjects().clear(); // lato diretto
        }

        // Ora puoi cancellare il tag
        tagRepository.delete(tag);
    }
}
