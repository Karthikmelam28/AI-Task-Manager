package com.karthik.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    private String description;

    @NotBlank(message = "Status cannot be empty")
    private String status;

}