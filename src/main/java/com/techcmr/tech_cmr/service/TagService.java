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
    private TaskRepository taskRepository;
    @Autowired
    private ProjectRepository projectRepository;
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
        Tag tag = tagMapper.toEntity(tagDTO);

        // Salviamo il Tag per avere l'ID (necessario per relazioni)
        tag = tagRepository.save(tag);

        // Relazioni con Task
        if (tagDTO.getTaskIds() != null && !tagDTO.getTaskIds().isEmpty()) {
            List<Task> tasks = taskRepository.findAllById(tagDTO.getTaskIds());
            for (Task task : tasks) {
                if (task.getTags() == null) {
                    task.setTags(new HashSet<>());
                }
                task.getTags().add(tag);  // aggiungi il tag al task
                taskRepository.save(task);
            }
            tag.setTasks(new HashSet<>(tasks)); // aggiorna anche il lato Tag
        }

        // Relazioni con Project
        if (tagDTO.getProjectIds() != null && !tagDTO.getProjectIds().isEmpty()) {
            List<Project> projects = projectRepository.findAllById(tagDTO.getProjectIds());
            for (Project project : projects) {
                if (project.getTags() == null) {
                    project.setTags(new HashSet<>());
                }
                project.getTags().add(tag);  // aggiungi il tag al project
                projectRepository.save(project);
            }
            tag.setProjects(new HashSet<>(projects)); // aggiorna anche il lato Tag
        }

        // Salva di nuovo il tag aggiornato con relazioni
        tag = tagRepository.save(tag);

        return tagMapper.toDto(tag);
    }

    // UPDATE
    public void updateTag(Long id, TagDTO tagDTO) {
        Tag tag = tagRepository.findWithTasksAndProjectsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));

        tag.setName(tagDTO.getName());

        // Aggiorno i Task associati: devo aggiornare i tags di ogni Task
        if (tagDTO.getTaskIds() != null) {
            // Prendo tutti i task in DB con gli id dati
            List<Task> tasks = taskRepository.findAllById(tagDTO.getTaskIds());

            // Per ogni task, aggiungo il tag (se non presente)
            for (Task t : tasks) {
                Set<Tag> tags = t.getTags();
                if (tags == null) tags = new HashSet<>();
                if (!tags.contains(tag)) {
                    tags.add(tag);
                    t.setTags(tags);
                    taskRepository.save(t);
                }
            }

            // Rimuovo il tag da tutti gli altri task che prima lo avevano ma ora non più
            Set<Task> oldTasks = tag.getTasks();
            if (oldTasks != null) {
                for (Task oldTask : oldTasks) {
                    if (!tasks.contains(oldTask)) {
                        Set<Tag> oldTags = oldTask.getTags();
                        if (oldTags != null && oldTags.contains(tag)) {
                            oldTags.remove(tag);
                            oldTask.setTags(oldTags);
                            taskRepository.save(oldTask);
                        }
                    }
                }
            }
        }

        // Lo stesso per Project
        if (tagDTO.getProjectIds() != null) {
            List<Project> projects = projectRepository.findAllById(tagDTO.getProjectIds());

            for (Project p : projects) {
                Set<Tag> tags = p.getTags();
                if (tags == null) tags = new HashSet<>();
                if (!tags.contains(tag)) {
                    tags.add(tag);
                    p.setTags(tags);
                    projectRepository.save(p);
                }
            }

            Set<Project> oldProjects = tag.getProjects();
            if (oldProjects != null) {
                for (Project oldProject : oldProjects) {
                    if (!projects.contains(oldProject)) {
                        Set<Tag> oldTags = oldProject.getTags();
                        if (oldTags != null && oldTags.contains(tag)) {
                            oldTags.remove(tag);
                            oldProject.setTags(oldTags);
                            projectRepository.save(oldProject);
                        }
                    }
                }
            }
        }

        tagRepository.save(tag);
    }

    // DELETE
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findWithTasksAndProjectsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));

        // Rimuovi associazione con Task
        if (tag.getTasks() != null) {
            for (Task task : tag.getTasks()) {
                task.getTags().remove(tag);
                taskRepository.save(task);
            }
        }

        // Rimuovi associazione con Project
        if (tag.getProjects() != null) {
            for (Project project : tag.getProjects()) {
                project.getTags().remove(tag);
                projectRepository.save(project);
            }
        }

        // Ora puoi cancellare il tag
        tagRepository.delete(tag);
    }
}
