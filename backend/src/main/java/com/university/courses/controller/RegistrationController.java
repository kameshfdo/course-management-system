package com.university.courses.controller;

import com.university.courses.dto.RegistrationDTO;
import com.university.courses.entity.Registration;
import com.university.courses.service.RegistrationService;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class RegistrationController {
    
    private final RegistrationService registrationService;
    
    @GetMapping
    public ResponseEntity<List<RegistrationDTO>> getAllRegistrations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.info("GET /api/registrations - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Registration> registrationPage = registrationService.getAllRegistrations(pageable);
        
        List<RegistrationDTO> registrationDTOs = registrationPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(registrationDTOs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RegistrationDTO> getRegistrationById(@PathVariable Long id) {
        log.info("GET /api/registrations/{}", id);
        Registration registration = registrationService.getRegistrationById(id);
        return ResponseEntity.ok(convertToDTO(registration));
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<RegistrationDTO>> getRegistrationsByStudentId(@PathVariable Long studentId) {
        log.info("GET /api/registrations/student/{}", studentId);
        List<Registration> registrations = registrationService.getRegistrationsByStudentId(studentId);
        List<RegistrationDTO> registrationDTOs = registrations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(registrationDTOs);
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<RegistrationDTO>> getRegistrationsByCourseId(@PathVariable Long courseId) {
        log.info("GET /api/registrations/course/{}", courseId);
        List<Registration> registrations = registrationService.getRegistrationsByCourseId(courseId);
        List<RegistrationDTO> registrationDTOs = registrations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(registrationDTOs);
    }
    
    @PostMapping
    public ResponseEntity<RegistrationDTO> createRegistration(@Valid @RequestBody RegistrationDTO registrationDTO) {
        log.info("POST /api/registrations - Creating registration for student: {} and course: {}", 
                registrationDTO.getStudentId(), registrationDTO.getCourseId());
        Registration registration = registrationService.createRegistration(
                registrationDTO.getStudentId(), 
                registrationDTO.getCourseId(),
                registrationDTO.getRemarks()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(registration));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RegistrationDTO> updateRegistration(@PathVariable Long id, @Valid @RequestBody RegistrationDTO registrationDTO) {
        log.info("PUT /api/registrations/{} - Updating registration", id);
        Registration updatedRegistration = registrationService.updateRegistration(id, 
                Registration.RegistrationStatus.valueOf(registrationDTO.getStatus()), 
                registrationDTO.getRemarks());
        return ResponseEntity.ok(convertToDTO(updatedRegistration));
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<RegistrationDTO> updateRegistrationStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        log.info("PUT /api/registrations/{}/status - Updating status to: {}", id, status);
        Registration.RegistrationStatus registrationStatus = Registration.RegistrationStatus.valueOf(status.toUpperCase());
        Registration updatedRegistration = registrationService.updateRegistrationStatus(id, registrationStatus);
        return ResponseEntity.ok(convertToDTO(updatedRegistration));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        log.info("DELETE /api/registrations/{}", id);
        registrationService.deleteRegistration(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RegistrationDTO>> getRegistrationsByStatus(@PathVariable String status) {
        log.info("GET /api/registrations/status/{}", status);
        Registration.RegistrationStatus registrationStatus = Registration.RegistrationStatus.valueOf(status.toUpperCase());
        List<Registration> registrations = registrationService.getRegistrationsByStatus(registrationStatus);
        List<RegistrationDTO> registrationDTOs = registrations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(registrationDTOs);
    }
    
    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<Long> getEnrolledCountByCourse(@PathVariable Long courseId) {
        log.info("GET /api/registrations/course/{}/count", courseId);
        long count = registrationService.getEnrolledCountByCourse(courseId);
        return ResponseEntity.ok(count);
    }
    
    private RegistrationDTO convertToDTO(Registration registration) {
        RegistrationDTO dto = new RegistrationDTO();
        dto.setId(registration.getId());
        dto.setStudentId(registration.getStudent().getId());
        dto.setStudentName(registration.getStudent().getFullName());
        dto.setCourseId(registration.getCourse().getId());
        dto.setCourseCode(registration.getCourse().getCode());
        dto.setCourseTitle(registration.getCourse().getTitle());
        dto.setRegistrationDate(registration.getRegistrationDate());
        dto.setStatus(registration.getStatus().name());
        dto.setRemarks(registration.getRemarks());
        return dto;
    }
}