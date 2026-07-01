package com.karthik.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {

    private Long id;

    private String title;

    private String description;

    private String status;

}