package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.TagDTO;
import com.techcmr.tech_cmr.mapper.TagMapper;
import com.techcmr.tech_cmr.model.Project;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.relations.TagRelationManager;
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
    private TagRelationManager tagRelationManager;

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
        Tag tag = tagRepository.findWithTasksAndProjectsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));
        return tagMapper.toDto(tag);
    }

    // CREATE
    public TagDTO createTag(TagDTO tagDTO) {

        // 1
        Tag tag = tagMapper.toEntity(tagDTO, projectRepository, taskRepository);

        // 2
        tagRelationManager.updateRelationsForPostTag(tag);

        // 3
        tag = tagRepository.save(tag);

        // 4
        return tagMapper.toDto(tag);
    }

    // UPDATE
    public void updateTag(Long id, TagDTO tagDTO) {
        // 1
        Tag tag = tagRepository.findWithTasksAndProjectsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));

        // 2
        tagMapper.updateEntityFromDto(tagDTO, tag);

        // 3
        tagRelationManager.updatesProjectsForTag(tag, tagDTO);
        tagRelationManager.updatesTasksForTag(tag, tagDTO);

        // 4
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
