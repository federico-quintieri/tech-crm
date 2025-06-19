package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.AttachmentDTO;
import com.techcmr.tech_cmr.mapper.AttachmentMapper;
import com.techcmr.tech_cmr.model.Attachment;
import com.techcmr.tech_cmr.repository.AttachmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private AttachmentMapper attachmentMapper;

    // Trova tutti gli attachment
    public List<AttachmentDTO> findAllAttachments() {
        return attachmentRepository.findAll().stream().map(attachmentMapper::toDto).toList();
    }

    // Trova un attachment in base ad id
    public AttachmentDTO findAttachmentById(Long id) {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Attachment not found"));
        return attachmentMapper.toDto(attachment);
    }

    // Crea attachment
    public AttachmentDTO saveAttachment(AttachmentDTO attachmentDTO) {
        Attachment attachment = attachmentMapper.toEntity(attachmentDTO); // Converto DTO body in entity
        Attachment savedAttachment = attachmentRepository.save(attachment); // Salvo l'entity
        return attachmentMapper.toDto(savedAttachment); // Ritorno il DTO
    }

    // Modifica attachement
    public void updateAttachment(Long id, AttachmentDTO attachmentDTO) {

        // 1 Trovo l'entity esistente
        Attachment updatedAttachment = attachmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Attachment not found"));

        // 2 Applico i nuovi valori ( merge tra DTO e entity esistente)
        attachmentMapper.updateEntityFromDto(attachmentDTO, updatedAttachment);

        // 3 Salvo nel db
        attachmentRepository.save(updatedAttachment);
    }

    // Cancella attachment
    public void deleteAttachement(Long id) {
        attachmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Attachment not found"));
        attachmentRepository.deleteById(id);
    }
}
