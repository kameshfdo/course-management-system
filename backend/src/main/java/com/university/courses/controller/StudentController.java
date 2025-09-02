package com.university.courses.controller;

import com.university.courses.dto.StudentDTO;
import com.university.courses.entity.Student;
import com.university.courses.service.StudentService;
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
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class StudentController {
    
    private final StudentService studentService;
    
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.info("GET /api/students - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Student> studentPage = studentService.getAllStudents(pageable);
        
        List<StudentDTO> studentDTOs = studentPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(studentDTOs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        log.info("GET /api/students/{}", id);
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(convertToDTO(student));
    }
    
    @GetMapping("/studentId/{studentId}")
    public ResponseEntity<StudentDTO> getStudentByStudentId(@PathVariable String studentId) {
        log.info("GET /api/students/studentId/{}", studentId);
        Student student = studentService.getStudentByStudentId(studentId);
        return ResponseEntity.ok(convertToDTO(student));
    }
    
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        log.info("POST /api/students - Creating student: {}", studentDTO.getStudentId());
        Student student = convertToEntity(studentDTO);
        Student savedStudent = studentService.createStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedStudent));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO studentDTO) {
        log.info("PUT /api/students/{} - Updating student", id);
        Student student = convertToEntity(studentDTO);
        Student updatedStudent = studentService.updateStudent(id, student);
        return ResponseEntity.ok(convertToDTO(updatedStudent));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        log.info("DELETE /api/students/{}", id);
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<StudentDTO>> searchStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Integer enrollmentYear,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("GET /api/students/search - name: {}, department: {}, enrollmentYear: {}", 
                name, department, enrollmentYear);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
        Page<Student> studentPage = studentService.searchStudents(name, department, enrollmentYear, pageable);
        
        List<StudentDTO> studentDTOs = studentPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(studentDTOs);
    }
    
    @GetMapping("/departments")
    public ResponseEntity<List<String>> getAllDepartments() {
        log.info("GET /api/students/departments");
        List<String> departments = studentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }
    
    @GetMapping("/enrollment-years")
    public ResponseEntity<List<Integer>> getAllEnrollmentYears() {
        log.info("GET /api/students/enrollment-years");
        List<Integer> years = studentService.getAllEnrollmentYears();
        return ResponseEntity.ok(years);
    }
    
    private StudentDTO convertToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setStudentId(student.getStudentId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setPhoneNumber(student.getPhoneNumber());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setDepartment(student.getDepartment());
        dto.setEnrollmentYear(student.getEnrollmentYear());
        dto.setCreatedAt(student.getCreatedAt());
        dto.setUpdatedAt(student.getUpdatedAt());
        return dto;
    }
    
    private Student convertToEntity(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setStudentId(dto.getStudentId());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setDepartment(dto.getDepartment());
        student.setEnrollmentYear(dto.getEnrollmentYear());
        return student;
    }
}