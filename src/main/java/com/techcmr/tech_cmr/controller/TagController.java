package com.techcmr.tech_cmr.controller;

import com.techcmr.tech_cmr.dto.TagDTO;
import com.techcmr.tech_cmr.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    // GET /tags
    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        List<TagDTO> tags = tagService.findAllTags();
        return ResponseEntity.ok(tags);
    }

    // GET /tags/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTagById(@PathVariable Long id) {
        TagDTO tag = tagService.findTagById(id);
        return ResponseEntity.ok(tag);
    }

    // POST /tags
    @PostMapping
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        TagDTO createdTag = tagService.createTag(tagDTO);
        return ResponseEntity.status(201).body(createdTag);
    }

    // PUT /tags/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTag(@PathVariable Long id, @RequestBody TagDTO tagDTO) {
        tagService.updateTag(id, tagDTO);
        return ResponseEntity.noContent().build();
    }

    // DELETE /tags/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
