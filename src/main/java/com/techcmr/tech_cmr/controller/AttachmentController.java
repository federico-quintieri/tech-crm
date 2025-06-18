package com.techcmr.tech_cmr.controller;

import com.techcmr.tech_cmr.dto.AttachmentDTO;
import com.techcmr.tech_cmr.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attachments")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    // READ
    @GetMapping
    public ResponseEntity<List<AttachmentDTO>> getAllAttachments() {
        List<AttachmentDTO> attachments = attachmentService.findAllAttachments();
        if (attachments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(attachments, HttpStatus.OK);
    }

    // READ
    @GetMapping("/{id}")
    public ResponseEntity<AttachmentDTO> getAttachmentById(@PathVariable("id") long id) {
        AttachmentDTO attachmentDTO = attachmentService.findAttachmentById(id);
        if (attachmentDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(attachmentDTO, HttpStatus.OK);
    }

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<AttachmentDTO> createAttachment(@RequestBody AttachmentDTO attachmentDTO) {
        if (attachmentDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AttachmentDTO newAttachmentDTO = attachmentService.saveAttachment(attachmentDTO);
        return ResponseEntity.ok(newAttachmentDTO);
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<AttachmentDTO> updateAttachmentById(@PathVariable Long id, @RequestBody AttachmentDTO attachmentDTO) {
        attachmentDTO.setId(id);
        AttachmentDTO newAttachmentDTO = attachmentService.saveAttachment(attachmentDTO);
        return ResponseEntity.ok(newAttachmentDTO);
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAttachmentById(@PathVariable Long id) {
        attachmentService.deleteAttachement(id);
        return new ResponseEntity<>("Attachment deleted successfully", HttpStatus.OK);
    }

}
