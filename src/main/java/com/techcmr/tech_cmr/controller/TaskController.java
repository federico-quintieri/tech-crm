package com.techcmr.tech_cmr.controller;

import com.techcmr.tech_cmr.dto.TaskDTO;
import com.techcmr.tech_cmr.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // GET /tasks - Lista di tutti i task
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.findAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // GET /tasks/{id} - Dettagli di un task
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        try {
            TaskDTO task = taskService.findTaskById(id);
            return ResponseEntity.ok(task);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /tasks - Crea un nuovo task
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.ok(createdTask);
    }

    // PUT /tasks/{id} - Aggiorna un task esistente
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        try {
            taskService.updateTask(id, taskDTO);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /tasks/{id} - Elimina un task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
