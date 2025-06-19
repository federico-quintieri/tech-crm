package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.TagDTO;
import com.techcmr.tech_cmr.mapper.TagMapper;
import com.techcmr.tech_cmr.model.Tag;
import com.techcmr.tech_cmr.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagMapper tagMapper;

    // READ
    public List<TagDTO> findAllTags() {
        return tagRepository.findAll().stream().map(tagMapper::toDto).toList();
    }

    // SHOW
    public TagDTO findTagById(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found"));
        return tagMapper.toDto(tag);
    }

    // CREATE
    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = tagMapper.toEntity(tagDTO);
        tag = tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    // UPDATE
    public void updateTag(Long id, TagDTO tagDTO) {
        Tag updatedtag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found"));
        tagMapper.updateEntityFromDto(tagDTO, updatedtag);
        tagRepository.save(updatedtag);
    }

    // DELETE
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found"));
        tagRepository.delete(tag);
    }
}
