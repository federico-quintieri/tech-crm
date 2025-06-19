package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.StoryDTO;
import com.techcmr.tech_cmr.mapper.StoryMapper;
import com.techcmr.tech_cmr.model.Story;
import com.techcmr.tech_cmr.repository.StoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private StoryMapper storyMapper;

    // READ
    public List<StoryDTO> findAllStories() {
        return storyRepository.findAll().stream().map(storyMapper::toDto).toList();
    }

    // SHOW
    public StoryDTO findStoryById(Long id) {
        // Trovo la story o lancio eccezione
        Story story = storyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Story not found"));
        return storyMapper.toDto(story);
    }

    // CREATE
    public StoryDTO createStory(StoryDTO storyDTO) {
        // Da DTO ad entity
        Story story = storyMapper.toEntity(storyDTO);
        // Salvo story
        storyRepository.save(story);
        // La rimetto ad entity per il controller
        return storyMapper.toDto(story);
    }

    // UPDATE
    public void updateStory(Long id, StoryDTO storyDTO) {
        // Trovo dal db la entity con determinato id
        Story updatedStory = storyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Story not found"));

        // Merge entity con dto
        storyMapper.updateEntityFromDto(storyDTO, updatedStory);

        // La salvo nel db
        storyRepository.save(updatedStory);

    }

    // DELETE
    public void deleteStory(Long id) {
        Story story = storyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Story not found"));
        storyRepository.delete(story);
    }

}
