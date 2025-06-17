package com.techcmr.tech_cmr.repository;

import com.techcmr.tech_cmr.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
