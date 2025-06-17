package com.techcmr.tech_cmr.repository;

import com.techcmr.tech_cmr.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {

}
