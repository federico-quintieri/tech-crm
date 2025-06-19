package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.SectionDTO;
import com.techcmr.tech_cmr.mapper.SectionMapper;
import com.techcmr.tech_cmr.model.Section;
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
        Section section = sectionMapper.toEntity(sectionDTO);
        Section savedSection = sectionRepository.save(section);
        return sectionMapper.toDto(savedSection);
    }

    // UPDATE
    public void updateSection(Long id, SectionDTO sectionDTO) {

        // Recupero la section esistente
        Section existingSection = sectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Applico i nuovi valori dal DTO all'entità esistente
        sectionMapper.updateEntityFromDto(sectionDTO, existingSection);

        // Salvo l'entità aggiornata
        sectionRepository.save(existingSection);
    }

    // DELETE
    public void deleteSection(Long id) {
        Section existingSection = sectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        sectionRepository.delete(existingSection);
    }
}
