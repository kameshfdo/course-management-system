package com.university.courses.controller;

import com.university.courses.dto.ResultDTO;
import com.university.courses.entity.Result;
import com.university.courses.service.ResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ResultController {
    
    private final ResultService resultService;
    
    @GetMapping
    public ResponseEntity<List<ResultDTO>> getAllResults(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.info("GET /api/results - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Result> resultPage = resultService.getAllResults(pageable);
        
        List<ResultDTO> resultDTOs = resultPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(resultDTOs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ResultDTO> getResultById(@PathVariable Long id) {
        log.info("GET /api/results/{}", id);
        Result result = resultService.getResultById(id);
        return ResponseEntity.ok(convertToDTO(result));
    }
    
    @GetMapping("/registration/{registrationId}")
    public ResponseEntity<ResultDTO> getResultByRegistrationId(@PathVariable Long registrationId) {
        log.info("GET /api/results/registration/{}", registrationId);
        Result result = resultService.getResultByRegistrationId(registrationId);
        return ResponseEntity.ok(convertToDTO(result));
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ResultDTO>> getResultsByStudentId(@PathVariable Long studentId) {
        log.info("GET /api/results/student/{}", studentId);
        List<Result> results = resultService.getResultsByStudentId(studentId);
        List<ResultDTO> resultDTOs = results.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultDTOs);
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ResultDTO>> getResultsByCourseId(@PathVariable Long courseId) {
        log.info("GET /api/results/course/{}", courseId);
        List<Result> results = resultService.getResultsByCourseId(courseId);
        List<ResultDTO> resultDTOs = results.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultDTOs);
    }
    
    @PostMapping
    public ResponseEntity<ResultDTO> createResult(@Valid @RequestBody ResultDTO resultDTO) {
        log.info("POST /api/results - Creating result for registration: {}", resultDTO.getRegistrationId());
        Result savedResult = resultService.createResult(
                resultDTO.getRegistrationId(),
                resultDTO.getMarks(),
                resultDTO.getFeedback()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedResult));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ResultDTO> updateResult(@PathVariable Long id, @Valid @RequestBody ResultDTO resultDTO) {
        log.info("PUT /api/results/{} - Updating result", id);
        Result updatedResult = resultService.updateResult(id, resultDTO.getMarks(), resultDTO.getFeedback());
        return ResponseEntity.ok(convertToDTO(updatedResult));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        log.info("DELETE /api/results/{}", id);
        resultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/student/{studentId}/gpa")
    public ResponseEntity<BigDecimal> getStudentGPA(@PathVariable Long studentId) {
        log.info("GET /api/results/student/{}/gpa", studentId);
        BigDecimal gpa = resultService.getAverageGPAByStudentId(studentId);
        return ResponseEntity.ok(gpa);
    }
    
    @GetMapping("/course/{courseId}/average")
    public ResponseEntity<BigDecimal> getCourseAverageMarks(@PathVariable Long courseId) {
        log.info("GET /api/results/course/{}/average", courseId);
        BigDecimal averageMarks = resultService.getAverageMarksByCourseId(courseId);
        return ResponseEntity.ok(averageMarks);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ResultDTO>> searchResultsByMarksRange(
            @RequestParam BigDecimal minMarks,
            @RequestParam BigDecimal maxMarks,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("GET /api/results/search - minMarks: {}, maxMarks: {}", minMarks, maxMarks);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("marks").descending());
        Page<Result> resultPage = resultService.getResultsByMarksRange(minMarks, maxMarks, pageable);
        
        List<ResultDTO> resultDTOs = resultPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(resultDTOs);
    }
    
    private ResultDTO convertToDTO(Result result) {
        ResultDTO dto = new ResultDTO();
        dto.setId(result.getId());
        dto.setRegistrationId(result.getRegistration().getId());
        dto.setStudentId(result.getRegistration().getStudent().getId());
        dto.setStudentName(result.getRegistration().getStudent().getFullName());
        dto.setCourseId(result.getRegistration().getCourse().getId());
        dto.setCourseCode(result.getRegistration().getCourse().getCode());
        dto.setCourseTitle(result.getRegistration().getCourse().getTitle());
        dto.setMarks(result.getMarks());
        dto.setGrade(result.getGrade());
        dto.setGpaPoints(result.getGpaPoints());
        dto.setFeedback(result.getFeedback());
        dto.setResultDate(result.getResultDate());
        dto.setCreatedAt(result.getCreatedAt());
        dto.setUpdatedAt(result.getUpdatedAt());
        return dto;
    }
}