package com.techcmr.tech_cmr.repository;

import com.techcmr.tech_cmr.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
