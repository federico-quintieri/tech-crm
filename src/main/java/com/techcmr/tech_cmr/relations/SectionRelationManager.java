package com.techcmr.tech_cmr.relations;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.techcmr.tech_cmr.dto.SectionDTO;
import com.techcmr.tech_cmr.model.Section;
import com.techcmr.tech_cmr.model.Task;
import com.techcmr.tech_cmr.repository.TaskRepository;

@Component
public class SectionRelationManager {

    private final TaskRepository taskRepository;

    public SectionRelationManager(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Metodo per aggiornare le Task collegate a Section
    public void updateTasksForSection(Section section, SectionDTO dto) {
        Set<Task> existingTasks = new HashSet<>(section.getTasks());
        Set<Task> updatedTasks = new HashSet<>();

        if (dto.getTaskIds() != null) {
            for (Long taskId : dto.getTaskIds()) {
                Task task = taskRepository.findById(taskId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
                task.setSection(section);
                updatedTasks.add(task);
            }
        }
        for (Task oldTask : existingTasks) {
            if (!updatedTasks.contains(oldTask)) {
                oldTask.setSection(null);
            }
        }
        section.setTasks(updatedTasks);
    }

    // Metodo per aggiornare task a chiamata POST
    public void updateRelationsForPostSection(Section section){

    }
}
