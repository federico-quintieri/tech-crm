package com.techcmr.tech_cmr.controller;

import com.techcmr.tech_cmr.dto.AttachmentDTO;
import com.techcmr.tech_cmr.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attachments")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    // GET all
    @GetMapping
    public ResponseEntity<List<AttachmentDTO>> getAllAttachments() {
        List<AttachmentDTO> attachments = attachmentService.findAllAttachments();
        return attachments.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(attachments);
    }

    // GET by id
    @GetMapping("/{id}")
    public ResponseEntity<AttachmentDTO> getAttachmentById(@PathVariable Long id) {
        AttachmentDTO attachmentDTO = attachmentService.findAttachmentById(id);
        return ResponseEntity.ok(attachmentDTO); // Se non trovato, il service lancia eccezione
    }

    // POST create
    @PostMapping
    public ResponseEntity<AttachmentDTO> createAttachment(@RequestBody AttachmentDTO attachmentDTO) {
        AttachmentDTO created = attachmentService.saveAttachment(attachmentDTO);
        return ResponseEntity.status(201).body(created);
    }

    // PUT update
    @PutMapping("/{id}")
    public ResponseEntity<AttachmentDTO> updateAttachment(@PathVariable Long id, @RequestBody AttachmentDTO attachmentDTO) {
        attachmentService.updateAttachment(id,attachmentDTO);
        return ResponseEntity.ok().build();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        attachmentService.deleteAttachement(id);
        return ResponseEntity.noContent().build();
    }

}
