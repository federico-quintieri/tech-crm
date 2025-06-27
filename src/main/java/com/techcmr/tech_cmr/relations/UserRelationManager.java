package com.techcmr.tech_cmr.relations;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.techcmr.tech_cmr.dto.UserDTO;
import com.techcmr.tech_cmr.model.Story;
import com.techcmr.tech_cmr.model.User;
import com.techcmr.tech_cmr.repository.StoryRepository;

@Component
public class UserRelationManager {

    private final StoryRepository storyRepository;

    public UserRelationManager(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    // Metodo che aggiorna storie collegate a user
    public void updateStoriesForUser(User user, UserDTO dto) {
        Set<Story> existingStories = new HashSet<>(user.getStories());
        Set<Story> updatedStories = new HashSet<>();

        if (dto.getStoryIds() != null) {
            for (Long storyId : dto.getStoryIds()) {
                Story story = storyRepository.findById(storyId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found"));
                story.setAuthor(user);
                updatedStories.add(story);
            }
        }
        for (Story oldStory : existingStories) {
            if (!updatedStories.contains(oldStory)) {
                oldStory.setAuthor(null);
            }
        }
        user.setStories(updatedStories);
    }

    // Metodo che aggiorna relazione a chiamata post
    public void updateRelationsForPostUser(User user) {
        if (user.getStories() != null) {
            for (Story story : user.getStories()) {
                story.setAuthor(user);
            }
        }
    }
}
