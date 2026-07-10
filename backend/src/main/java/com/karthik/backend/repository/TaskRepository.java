package com.karthik.backend.repository;

import com.karthik.backend.entity.Task;
import com.karthik.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository
        extends JpaRepository<Task, Long> {

    List<Task> findByStatus(String status);

    List<Task> findByUser(User user);

    Page<Task> findByUser(
            User user,
            Pageable pageable
    );
}