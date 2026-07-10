package com.karthik.backend.service;

import com.karthik.backend.dto.TaskResponseDTO;
import com.karthik.backend.entity.Task;
import com.karthik.backend.entity.User;
import com.karthik.backend.exception.TaskNotFoundException;
import com.karthik.backend.repository.TaskRepository;
import com.karthik.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(
            TaskRepository taskRepository,
            UserRepository userRepository) {

        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // GET ALL TASKS
    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {

        User user = getCurrentUser();

        // ADMIN can see all tasks
        if (user.getRole().equals("ADMIN")) {

            return taskRepository.findAll(pageable)
                    .map(this::mapToResponseDTO);
        }

        // USER can only see their tasks
        return taskRepository.findByUser(user, pageable)
                .map(this::mapToResponseDTO);
    }

    // GET TASKS BY STATUS
    public List<TaskResponseDTO> getTasksByStatus(String status) {

        User user = getCurrentUser();

        if (user.getRole().equals("ADMIN")) {

            return taskRepository.findByStatus(status)
                    .stream()
                    .map(this::mapToResponseDTO)
                    .toList();
        }

        return taskRepository.findByUser(user)
                .stream()
                .filter(task ->
                        task.getStatus().equalsIgnoreCase(status))
                .map(this::mapToResponseDTO)
                .toList();
    }

    // CREATE TASK
    public Task saveTask(Task task) {

        User user = getCurrentUser();

        task.setUser(user);

        return taskRepository.save(task);
    }

    // GET TASK BY ID
    public Task getTaskById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(id));

        User user = getCurrentUser();

        if (user.getRole().equals("ADMIN")) {
            return task;
        }

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You are not allowed to access this task");
        }

        return task;
    }

    // UPDATE TASK
    public Task updateTask(Long id,
                           Task updatedTask) {

        Task existingTask = getTaskById(id);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());

        return taskRepository.save(existingTask);
    }

    // DELETE TASK
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(id));

        taskRepository.delete(task);
    }

    // CURRENT LOGGED-IN USER
    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    // ENTITY → DTO
    private TaskResponseDTO mapToResponseDTO(
            Task task) {

        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus()
        );
    }
}