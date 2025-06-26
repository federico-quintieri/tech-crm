package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.SectionDTO;
import com.techcmr.tech_cmr.mapper.SectionMapper;
import com.techcmr.tech_cmr.model.Section;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.repository.ProjectRepository;
import com.techcmr.tech_cmr.repository.SectionRepository;
import com.techcmr.tech_cmr.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Section section = sectionMapper.toEntity(sectionDTO, projectRepository, taskRepository);
        if (section.getTasks() != null) {
            for (Task t : section.getTasks()) {
                t.setSection(section); // importantissimo per Hibernate
            }
        }
        Section saved = sectionRepository.save(section);
        return sectionMapper.toDto(saved);
    }

    // UPDATE
    public void updateSection(Long id, SectionDTO sectionDTO) {

        // 1. Trova la section esistente
        Section existing = sectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 2. Mergiare l'entity alla DTO
        sectionMapper.updateEntityFromDto(sectionDTO, existing, projectRepository);

        // 3. Copia i task vecchi
        Set<Task> existingTasks = new HashSet<>(existing.getTasks());

        // 4. Mappa i nuovi task dal DTO
        Set<Task> updatedTasks = new HashSet<>();
        if (sectionDTO.getTaskIds() != null) {

            for (Long taskId : sectionDTO.getTaskIds()) {

                Task task = taskRepository.findById(taskId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
                task.setSection(existing);
                updatedTasks.add(task);
            }

        }

        // 5. Rimuovi i task non più presenti
        for (Task oldTask : existingTasks) {
            if (!updatedTasks.contains(oldTask)) {
                oldTask.setSection(null); // Stacca da relazione
            }
        }

        // 6. Applica i nuovi task alla sezione
        existing.setTasks(updatedTasks);

        // 7. Salva la section
        sectionRepository.save(existing);
    }

    // DELETE
    public void deleteSection(Long id) {
        Section existingSection = sectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        sectionRepository.delete(existingSection);
    }
}
