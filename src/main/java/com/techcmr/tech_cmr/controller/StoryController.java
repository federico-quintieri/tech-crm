package com.techcmr.tech_cmr.controller;

import com.techcmr.tech_cmr.dto.StoryDTO;
import com.techcmr.tech_cmr.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stories")
public class StoryController {

    @Autowired
    private StoryService storyService;

    // GET /stories
    @GetMapping
    public ResponseEntity<List<StoryDTO>> getAllStories() {
        List<StoryDTO> stories = storyService.findAllStories();
        return ResponseEntity.ok(stories);
    }

    // GET /stories/{id}
    @GetMapping("/{id}")
    public ResponseEntity<StoryDTO> getStoryById(@PathVariable Long id) {
        StoryDTO story = storyService.findStoryById(id);
        return ResponseEntity.ok(story);
    }

    // POST /stories
    @PostMapping
    public ResponseEntity<StoryDTO> createStory(@RequestBody StoryDTO storyDTO) {
        StoryDTO createdStory = storyService.createStory(storyDTO);
        return ResponseEntity.status(201).body(createdStory);
    }

    // PUT /stories/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStory(@PathVariable Long id, @RequestBody StoryDTO storyDTO) {
        storyService.updateStory(id, storyDTO);
        return ResponseEntity.noContent().build();
    }

    // DELETE /stories/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable Long id) {
        storyService.deleteStory(id);
        return ResponseEntity.noContent().build();
    }
}
