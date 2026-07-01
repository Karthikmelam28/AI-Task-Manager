package com.karthik.backend.service;

import com.karthik.backend.dto.TaskResponseDTO;
import com.karthik.backend.entity.Task;
import com.karthik.backend.exception.TaskNotFoundException;
import com.karthik.backend.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // GET ALL TASKS WITH PAGINATION
    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {

        return taskRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }

    // GET TASKS BY STATUS
    public List<TaskResponseDTO> getTasksByStatus(String status) {

        return taskRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // CREATE TASK
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    // GET TASK BY ID
    public Task getTaskById(Long id) {

        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    // UPDATE TASK
    public Task updateTask(Long id, Task updatedTask) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());

        return taskRepository.save(existingTask);
    }

    // DELETE TASK
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.delete(task);
    }

    // ENTITY -> RESPONSE DTO
    private TaskResponseDTO mapToResponseDTO(Task task) {

        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus()
        );
    }
}