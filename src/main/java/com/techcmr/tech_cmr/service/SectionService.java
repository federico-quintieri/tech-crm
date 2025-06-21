package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.SectionDTO;
import com.techcmr.tech_cmr.mapper.SectionMapper;
import com.techcmr.tech_cmr.model.Section;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ProjectService projectService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private SectionMapper sectionMapper;

    // READ
    public List<SectionDTO> findAllSections() {
        return sectionRepository.findAll().stream().map(sectionMapper::toDto).toList();
    }

    // SHOW
    public SectionDTO findSectionById(Long id) {
        Section section = sectionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return sectionMapper.toDto(section);
    }

    // CREATE
    public SectionDTO createSection(SectionDTO sectionDTO) {
        Section section = sectionMapper.toEntity(sectionDTO, projectService, taskService);
        if (section.getTasks() != null) {
            for (Task t : section.getTasks()) {
                t.setSection(section);  // importantissimo per Hibernate
            }
        }
        Section saved = sectionRepository.save(section);
        return sectionMapper.toDto(saved);
    }

    // UPDATE
    public void updateSection(Long id, SectionDTO sectionDTO) {

        Section existing = sectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sectionMapper.updateEntityFromDto(sectionDTO, existing, projectService, taskService);
        if (existing.getTasks() != null) {
            for (Task t : existing.getTasks()) {
                t.setSection(existing);
            }
        }
        sectionRepository.save(existing);
    }

    // DELETE
    public void deleteSection(Long id) {
        Section existingSection = sectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        sectionRepository.delete(existingSection);
    }
}
