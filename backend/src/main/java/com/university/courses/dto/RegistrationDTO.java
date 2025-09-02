package com.university.courses.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {
    
    private Long id;
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    private String studentName;
    
    @NotNull(message = "Course ID is required")
    private Long courseId;
    
    private String courseCode;
    
    private String courseTitle;
    
    private LocalDateTime registrationDate;
    
    private String status;
    
    private String remarks;
}