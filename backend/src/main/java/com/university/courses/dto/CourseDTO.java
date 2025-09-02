package com.university.courses.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    
    private Long id;
    
    @NotBlank(message = "Course code is required")
    private String code;
    
    @NotBlank(message = "Course title is required")
    private String title;
    
    private String description;
    
    @NotNull(message = "Credits are required")
    @Positive(message = "Credits must be positive")
    private Integer credits;
    
    @NotBlank(message = "Department is required")
    private String department;
    
    private Integer maxEnrollment;
    
    private Integer currentEnrollment;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
