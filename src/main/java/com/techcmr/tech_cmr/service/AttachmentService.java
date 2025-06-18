package com.techcmr.tech_cmr.service;

import com.techcmr.tech_cmr.dto.AttachmentDTO;
import com.techcmr.tech_cmr.mapper.AttachmentMapper;
import com.techcmr.tech_cmr.model.Attachment;
import com.techcmr.tech_cmr.repository.AttachmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private AttachmentMapper attachmentMapper;

    // Trova tutti gli attachment
    public List<AttachmentDTO> findAllAttachments() {
        List<Attachment> attachments = attachmentRepository.findAll();

        if (attachments.isEmpty()) {
            throw new EntityNotFoundException("No attachments found");
        }

        return attachments.stream().
                map(attachmentMapper::toDto).
                toList();

    }

    // Trova un attachment in base ad id
    public AttachmentDTO findAttachmentById(Long id) {
        Optional<Attachment> attachment = attachmentRepository.findById(id);

        if (attachment.isEmpty()) {
            throw new EntityNotFoundException("Attachment not found");
        }
        return attachmentMapper.toDto(attachment.get());
    }

    // Crea attachment
    public AttachmentDTO saveAttachment(AttachmentDTO attachmentDTO) {
        Attachment attachment = attachmentMapper.toEntity(attachmentDTO); // Converto DTO body in entity
        Attachment savedAttachment = attachmentRepository.save(attachment); // Salvo l'entity
        return attachmentMapper.toDto(savedAttachment); // Ritorno il DTO
    }

    // Modifica attachement
    public void updateAttachment(AttachmentDTO attachmentDTO) {

        // 1 Trovo l'entity esistente
        Attachment existingAttachment = attachmentRepository.findById(attachmentDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Attachment not found"));

        // 2 Applico i nuovi valori ( merge tra DTO e entity esistente)
        attachmentMapper.updateEntityFromDto(attachmentDTO, existingAttachment);

        // 3 Salvo nel db
        attachmentRepository.save(existingAttachment);
    }

    // Cancella attachment
    public void deleteAttachement(Long id) {
        attachmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Attachment not found"));
        attachmentRepository.deleteById(id);
    }
}
