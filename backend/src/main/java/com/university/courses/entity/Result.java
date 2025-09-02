package com.university.courses.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private Registration registration;
    
    @DecimalMin(value = "0.0", message = "Marks cannot be negative")
    @DecimalMax(value = "100.0", message = "Marks cannot exceed 100")
    @Column(precision = 5, scale = 2)
    private BigDecimal marks;
    
    @Column(length = 2)
    private String grade;
    
    @Column(name = "gpa_points", precision = 3, scale = 2)
    private BigDecimal gpaPoints;
    
    @Column(length = 500)
    private String feedback;
    
    @Column(name = "result_date")
    private LocalDateTime resultDate;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (resultDate == null) {
            resultDate = LocalDateTime.now();
        }
        calculateGradeAndGPA();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateGradeAndGPA();
    }
    
    private void calculateGradeAndGPA() {
        if (marks != null) {
            double markValue = marks.doubleValue();
            if (markValue >= 90) {
                grade = "A+";
                gpaPoints = BigDecimal.valueOf(4.0);
            } else if (markValue >= 85) {
                grade = "A";
                gpaPoints = BigDecimal.valueOf(3.7);
            } else if (markValue >= 80) {
                grade = "A-";
                gpaPoints = BigDecimal.valueOf(3.3);
            } else if (markValue >= 75) {
                grade = "B+";
                gpaPoints = BigDecimal.valueOf(3.0);
            } else if (markValue >= 70) {
                grade = "B";
                gpaPoints = BigDecimal.valueOf(2.7);
            } else if (markValue >= 65) {
                grade = "B-";
                gpaPoints = BigDecimal.valueOf(2.3);
            } else if (markValue >= 60) {
                grade = "C+";
                gpaPoints = BigDecimal.valueOf(2.0);
            } else if (markValue >= 55) {
                grade = "C";
                gpaPoints = BigDecimal.valueOf(1.7);
            } else if (markValue >= 50) {
                grade = "C-";
                gpaPoints = BigDecimal.valueOf(1.3);
            } else {
                grade = "F";
                gpaPoints = BigDecimal.valueOf(0.0);
            }
        }
    }
}
