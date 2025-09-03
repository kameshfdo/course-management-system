package com.university.courses.controller;

import com.university.courses.dto.CourseDTO;
import com.university.courses.dto.RegistrationDTO;
import com.university.courses.dto.ResultDTO;
import com.university.courses.service.StudentPortalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('STUDENT')")
public class StudentPortalController {
    
    private final StudentPortalService studentPortalService;
    
    @GetMapping("/courses/available")
    public ResponseEntity<List<CourseDTO>> getAvailableCourses() {
        log.info("GET /api/student/courses/available - Getting available courses for student");
        return ResponseEntity.ok(studentPortalService.getAvailableCoursesForStudent());
    }
    
    @GetMapping("/courses/enrolled")
    public ResponseEntity<List<CourseDTO>> getEnrolledCourses() {
        log.info("GET /api/student/courses/enrolled - Getting enrolled courses for student");
        return ResponseEntity.ok(studentPortalService.getEnrolledCoursesForStudent());
    }
    
    @GetMapping("/registrations")
    public ResponseEntity<List<RegistrationDTO>> getMyRegistrations() {
        log.info("GET /api/student/registrations - Getting student registrations");
        return ResponseEntity.ok(studentPortalService.getStudentRegistrations());
    }
    
    @GetMapping("/results")
    public ResponseEntity<List<ResultDTO>> getMyResults() {
        log.info("GET /api/student/results - Getting student results");
        return ResponseEntity.ok(studentPortalService.getStudentResults());
    }
    
    @PostMapping("/courses/{courseId}/enroll")
    public ResponseEntity<RegistrationDTO> enrollInCourse(@PathVariable Long courseId) {
        log.info("POST /api/student/courses/{}/enroll - Student enrolling in course", courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentPortalService.enrollInCourse(courseId));
    }
    
    @PostMapping("/courses/{courseId}/unenroll")
    public ResponseEntity<Void> unenrollFromCourse(@PathVariable Long courseId) {
        log.info("POST /api/student/courses/{}/unenroll - Student unenrolling from course", courseId);
        studentPortalService.unenrollFromCourse(courseId);
        return ResponseEntity.noContent().build();
    }
}