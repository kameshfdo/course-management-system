package com.university.courses.service;

import com.university.courses.entity.Student;
import com.university.courses.exception.ResourceNotFoundException;
import com.university.courses.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StudentService {
    
    private final StudentRepository studentRepository;
    
    public List<Student> getAllStudents() {
        log.debug("Fetching all students");
        return studentRepository.findAll();
    }
    
    public Page<Student> getAllStudents(Pageable pageable) {
        log.debug("Fetching students with pagination: {}", pageable);
        return studentRepository.findAll(pageable);
    }
    
    public Student getStudentById(Long id) {
        log.debug("Fetching student by id: {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }
    
    public Student getStudentByStudentId(String studentId) {
        log.debug("Fetching student by student ID: {}", studentId);
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with student ID: " + studentId));
    }
    
    public Student getStudentByEmail(String email) {
        log.debug("Fetching student by email: {}", email);
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with email: " + email));
    }
    
    @Transactional
    public Student createStudent(Student student) {
        log.debug("Creating new student: {}", student.getStudentId());
        
        if (studentRepository.existsByStudentId(student.getStudentId())) {
            throw new IllegalArgumentException("Student with student ID " + student.getStudentId() + " already exists");
        }
        
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("Student with email " + student.getEmail() + " already exists");
        }
        
        return studentRepository.save(student);
    }
    
    @Transactional
    public Student updateStudent(Long id, Student updatedStudent) {
        log.debug("Updating student with id: {}", id);
        
        Student existingStudent = getStudentById(id);
        
        // Check if student ID is being changed and if new student ID already exists
        if (!existingStudent.getStudentId().equals(updatedStudent.getStudentId()) && 
            studentRepository.existsByStudentId(updatedStudent.getStudentId())) {
            throw new IllegalArgumentException("Student with student ID " + updatedStudent.getStudentId() + " already exists");
        }
        
        // Check if email is being changed and if new email already exists
        if (!existingStudent.getEmail().equals(updatedStudent.getEmail()) && 
            studentRepository.existsByEmail(updatedStudent.getEmail())) {
            throw new IllegalArgumentException("Student with email " + updatedStudent.getEmail() + " already exists");
        }
        
        existingStudent.setStudentId(updatedStudent.getStudentId());
        existingStudent.setFirstName(updatedStudent.getFirstName());
        existingStudent.setLastName(updatedStudent.getLastName());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setPhoneNumber(updatedStudent.getPhoneNumber());
        existingStudent.setDateOfBirth(updatedStudent.getDateOfBirth());
        existingStudent.setDepartment(updatedStudent.getDepartment());
        existingStudent.setEnrollmentYear(updatedStudent.getEnrollmentYear());
        
        return studentRepository.save(existingStudent);
    }
    
    @Transactional
    public void deleteStudent(Long id) {
        log.debug("Deleting student with id: {}", id);
        
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        
        studentRepository.deleteById(id);
    }
    
    public List<Student> getStudentsByDepartment(String department) {
        log.debug("Fetching students by department: {}", department);
        return studentRepository.findByDepartment(department);
    }
    
    public Page<Student> searchStudents(String name, String department, Integer enrollmentYear, Pageable pageable) {
        log.debug("Searching students with filters - name: {}, department: {}, enrollmentYear: {}", 
                 name, department, enrollmentYear);
        return studentRepository.findStudentsWithFilters(name, department, enrollmentYear, pageable);
    }
    
    public List<String> getAllDepartments() {
        log.debug("Fetching all departments");
        return studentRepository.findAllDepartments();
    }
    
    public List<Integer> getAllEnrollmentYears() {
        log.debug("Fetching all enrollment years");
        return studentRepository.findAllEnrollmentYears();
    }
    
    public boolean existsByStudentId(String studentId) {
        return studentRepository.existsByStudentId(studentId);
    }
    
    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }
}