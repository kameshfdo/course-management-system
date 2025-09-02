package com.university.courses.controller;

import com.university.courses.dto.CourseDTO;
import com.university.courses.entity.Course;
import com.university.courses.service.CourseService;
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
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {
    
    private final CourseService courseService;
    
    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.info("GET /api/courses - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Course> coursePage = courseService.getAllCourses(pageable);
        
        List<CourseDTO> courseDTOs = coursePage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(courseDTOs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        log.info("GET /api/courses/{}", id);
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(convertToDTO(course));
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<CourseDTO> getCourseByCode(@PathVariable String code) {
        log.info("GET /api/courses/code/{}", code);
        Course course = courseService.getCourseByCode(code);
        return ResponseEntity.ok(convertToDTO(course));
    }
    
    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        log.info("POST /api/courses - Creating course: {}", courseDTO.getCode());
        Course course = convertToEntity(courseDTO);
        Course savedCourse = courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedCourse));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDTO courseDTO) {
        log.info("PUT /api/courses/{} - Updating course", id);
        Course course = convertToEntity(courseDTO);
        Course updatedCourse = courseService.updateCourse(id, course);
        return ResponseEntity.ok(convertToDTO(updatedCourse));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.info("DELETE /api/courses/{}", id);
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<CourseDTO>> searchCourses(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Integer minCredits,
            @RequestParam(required = false) Integer maxCredits,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("GET /api/courses/search - title: {}, department: {}, minCredits: {}, maxCredits: {}", 
                title, department, minCredits, maxCredits);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<Course> coursePage = courseService.searchCourses(title, department, minCredits, maxCredits, pageable);
        
        List<CourseDTO> courseDTOs = coursePage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(courseDTOs);
    }
    
    @GetMapping("/departments")
    public ResponseEntity<List<String>> getAllDepartments() {
        log.info("GET /api/courses/departments");
        List<String> departments = courseService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }
    
    private CourseDTO convertToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setCode(course.getCode());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setCredits(course.getCredits());
        dto.setDepartment(course.getDepartment());
        dto.setMaxEnrollment(course.getMaxEnrollment());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());
        // Calculate current enrollment if needed
        dto.setCurrentEnrollment(course.getRegistrations() != null ? 
                (int) course.getRegistrations().stream()
                        .filter(reg -> reg.getStatus() == com.university.courses.entity.Registration.RegistrationStatus.ENROLLED)
                        .count() : 0);
        return dto;
    }
    
    private Course convertToEntity(CourseDTO dto) {
        Course course = new Course();
        course.setId(dto.getId());
        course.setCode(dto.getCode());
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setCredits(dto.getCredits());
        course.setDepartment(dto.getDepartment());
        course.setMaxEnrollment(dto.getMaxEnrollment());
        return course;
    }
}