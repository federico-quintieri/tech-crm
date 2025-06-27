package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.SectionDTO;
import com.techcmr.tech_cmr.mapper.SectionMapper;
import com.techcmr.tech_cmr.model.Section;
import com.techcmr.tech_cmr.relations.SectionRelationManager;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import com.techcmr.tech_cmr.repository.SectionRepository;
import com.techcmr.tech_cmr.repository.TaskRepository;

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
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private SectionRelationManager sectionRelationManager;

    // READ
    public List<SectionDTO> findAllSections() {
        return sectionRepository.findAll().stream().map(sectionMapper::toDto).toList();
    }

    // SHOW
    public SectionDTO findSectionById(Long id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return sectionMapper.toDto(section);
    }

    // CREATE
    public SectionDTO createSection(SectionDTO sectionDTO) {

        // 1. Da dto a entity
        Section section = sectionMapper.toEntity(sectionDTO, projectRepository, taskRepository);

        // 2. Metodo per aggiornare relazione one to many
        sectionRelationManager.updateRelationsForPostSection(section);

        // 3. Salvo la nuova section
        Section saved = sectionRepository.save(section);

        // 4. Restituisco il dto
        return sectionMapper.toDto(saved);
    }

    // UPDATE
    public void updateSection(Long id, SectionDTO sectionDTO) {

        // 1. Trova la section esistente
        Section existing = sectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 2. Mergiare l'entity alla DTO
        sectionMapper.updateEntityFromDto(sectionDTO, existing, projectRepository);

        // 3. Metodo per aggiornare la task collegata
        sectionRelationManager.updateTasksForSection(existing, sectionDTO);

        // 4. Salva la section
        sectionRepository.save(existing);
    }

    // DELETE
    public void deleteSection(Long id) {
        Section existingSection = sectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        sectionRepository.delete(existingSection);
    }
}
