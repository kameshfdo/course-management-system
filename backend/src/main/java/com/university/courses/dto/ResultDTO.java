package com.university.courses.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDTO {
    
    private Long id;
    
    @NotNull(message = "Registration ID is required")
    private Long registrationId;
    
    private Long studentId;
    
    private String studentName;
    
    private Long courseId;
    
    private String courseCode;
    
    private String courseTitle;
    
    @DecimalMin(value = "0.0", message = "Marks cannot be negative")
    @DecimalMax(value = "100.0", message = "Marks cannot exceed 100")
    private BigDecimal marks;
    
    private String grade;
    
    private BigDecimal gpaPoints;
    
    private String feedback;
    
    private LocalDateTime resultDate;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}