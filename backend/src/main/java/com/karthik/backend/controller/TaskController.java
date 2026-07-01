package com.karthik.backend.controller;

import com.karthik.backend.api.ApiResponse;
import com.karthik.backend.dto.TaskRequestDTO;
import com.karthik.backend.dto.TaskResponseDTO;
import com.karthik.backend.entity.Task;
import com.karthik.backend.service.TaskService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // GET ALL TASKS WITH PAGINATION
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> getAllTasks(Pageable pageable) {

        Page<TaskResponseDTO> tasks = taskService.getAllTasks(pageable);

        ApiResponse<Page<TaskResponseDTO>> response =
                new ApiResponse<>(
                        true,
                        "Tasks fetched successfully",
                        tasks
                );

        return ResponseEntity.ok(response);
    }

    // GET TASKS BY STATUS
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getTasksByStatus(
            @PathVariable String status) {

        List<TaskResponseDTO> tasks = taskService.getTasksByStatus(status);

        ApiResponse<List<TaskResponseDTO>> response =
                new ApiResponse<>(
                        true,
                        "Tasks fetched successfully",
                        tasks
                );

        return ResponseEntity.ok(response);
    }

    // CREATE TASK
    @PostMapping
    public Task createTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO) {

        Task task = new Task();

        task.setTitle(taskRequestDTO.getTitle());
        task.setDescription(taskRequestDTO.getDescription());
        task.setStatus(taskRequestDTO.getStatus());

        return taskService.saveTask(task);
    }

    // GET TASK BY ID
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {

        return taskService.getTaskById(id);
    }

    // UPDATE TASK
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id,
                           @RequestBody Task task) {

        return taskService.updateTask(id, task);
    }

    // DELETE TASK
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {

        taskService.deleteTask(id);

        return "Task deleted successfully!";
    }
}