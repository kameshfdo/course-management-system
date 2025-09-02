package com.university.courses.service;

import com.university.courses.entity.Registration;
import com.university.courses.entity.Result;
import com.university.courses.exception.ResourceNotFoundException;
import com.university.courses.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ResultService {
    
    private final ResultRepository resultRepository;
    private final RegistrationService registrationService;
    
    public List<Result> getAllResults() {
        log.debug("Fetching all results");
        return resultRepository.findAll();
    }
    
    public Page<Result> getAllResults(Pageable pageable) {
        log.debug("Fetching results with pagination: {}", pageable);
        return resultRepository.findAll(pageable);
    }
    
    public Result getResultById(Long id) {
        log.debug("Fetching result by id: {}", id);
        return resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found with id: " + id));
    }
    
    public Result getResultByRegistrationId(Long registrationId) {
        log.debug("Fetching result by registration id: {}", registrationId);
        return resultRepository.findByRegistrationId(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found for registration id: " + registrationId));
    }
    
    public List<Result> getResultsByStudentId(Long studentId) {
        log.debug("Fetching results by student id: {}", studentId);
        return resultRepository.findByStudentIdWithDetails(studentId);
    }
    
    public List<Result> getResultsByCourseId(Long courseId) {
        log.debug("Fetching results by course id: {}", courseId);
        return resultRepository.findByCourseIdWithDetails(courseId);
    }
    
    @Transactional
    public Result createResult(Long registrationId, BigDecimal marks, String feedback) {
        log.debug("Creating result for registration: {}", registrationId);
        
        // Check if result already exists for this registration
        if (resultRepository.findByRegistrationId(registrationId).isPresent()) {
            throw new IllegalArgumentException("Result already exists for registration id: " + registrationId);
        }
        
        // Get the registration
        Registration registration = registrationService.getRegistrationById(registrationId);
        
        Result result = new Result();
        result.setRegistration(registration);
        result.setMarks(marks);
        result.setFeedback(feedback);
        
        return resultRepository.save(result);
    }
    
    @Transactional
    public Result updateResult(Long id, BigDecimal marks, String feedback) {
        log.debug("Updating result with id: {}", id);
        
        Result existingResult = getResultById(id);
        existingResult.setMarks(marks);
        existingResult.setFeedback(feedback);
        
        return resultRepository.save(existingResult);
    }
    
    @Transactional
    public void deleteResult(Long id) {
        log.debug("Deleting result with id: {}", id);
        
        if (!resultRepository.existsById(id)) {
            throw new ResourceNotFoundException("Result not found with id: " + id);
        }
        
        resultRepository.deleteById(id);
    }
    
    public BigDecimal getAverageMarksByCourseId(Long courseId) {
        log.debug("Getting average marks for course: {}", courseId);
        BigDecimal average = resultRepository.findAverageMarksByCourseId(courseId);
        return average != null ? average : BigDecimal.ZERO;
    }
    
    public BigDecimal getAverageGPAByStudentId(Long studentId) {
        log.debug("Getting average GPA for student: {}", studentId);
        BigDecimal average = resultRepository.findAverageGPAByStudentId(studentId);
        return average != null ? average : BigDecimal.ZERO;
    }
    
    public Page<Result> getResultsByMarksRange(BigDecimal minMarks, BigDecimal maxMarks, Pageable pageable) {
        log.debug("Getting results by marks range: {} to {}", minMarks, maxMarks);
        return resultRepository.findByMarksRange(minMarks, maxMarks, pageable);
    }
    
    public boolean hasResultForRegistration(Long registrationId) {
        return resultRepository.findByRegistrationId(registrationId).isPresent();
    }
}