package com.techcmr.tech_cmr.controller;

import com.techcmr.tech_cmr.dto.SectionDTO;
import com.techcmr.tech_cmr.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    // GET /sections
    @GetMapping
    public ResponseEntity<List<SectionDTO>> getAllSections() {
        List<SectionDTO> sections = sectionService.findAllSections();
        return ResponseEntity.ok(sections);
    }

    // GET /sections/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SectionDTO> getSectionById(@PathVariable Long id) {
        SectionDTO section = sectionService.findSectionById(id);
        return ResponseEntity.ok(section);
    }

    // POST /sections
    @PostMapping
    public ResponseEntity<SectionDTO> createSection(@RequestBody SectionDTO sectionDTO) {
        SectionDTO createdSection = sectionService.createSection(sectionDTO);
        return ResponseEntity.status(201).body(createdSection);
    }

    // PUT /sections/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSection(@PathVariable Long id, @RequestBody SectionDTO sectionDTO) {
        sectionService.updateSection(id, sectionDTO);
        return ResponseEntity.noContent().build();
    }

    // DELETE /sections/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
        return ResponseEntity.noContent().build();
    }
}
