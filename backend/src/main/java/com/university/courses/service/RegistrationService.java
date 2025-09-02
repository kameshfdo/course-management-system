package com.university.courses.service;

import com.university.courses.entity.Course;
import com.university.courses.entity.Registration;
import com.university.courses.entity.Student;
import com.university.courses.exception.ResourceNotFoundException;
import com.university.courses.repository.RegistrationRepository;
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
public class RegistrationService {
    
    private final RegistrationRepository registrationRepository;
    private final StudentService studentService;
    private final CourseService courseService;
    
    public List<Registration> getAllRegistrations() {
        log.debug("Fetching all registrations");
        return registrationRepository.findAll();
    }
    
    public Page<Registration> getAllRegistrations(Pageable pageable) {
        log.debug("Fetching registrations with pagination: {}", pageable);
        return registrationRepository.findAll(pageable);
    }
    
    public Registration getRegistrationById(Long id) {
        log.debug("Fetching registration by id: {}", id);
        return registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with id: " + id));
    }
    
    public List<Registration> getRegistrationsByStudentId(Long studentId) {
        log.debug("Fetching registrations by student id: {}", studentId);
        return registrationRepository.findByStudentIdWithDetails(studentId);
    }
    
    public List<Registration> getRegistrationsByCourseId(Long courseId) {
        log.debug("Fetching registrations by course id: {}", courseId);
        return registrationRepository.findByCourseIdWithDetails(courseId);
    }
    
    public List<Registration> getRegistrationsByStatus(Registration.RegistrationStatus status) {
        log.debug("Fetching registrations by status: {}", status);
        return registrationRepository.findByStatus(status);
    }
    
    @Transactional
    public Registration createRegistration(Long studentId, Long courseId, String remarks) {
        log.debug("Creating registration for student: {} and course: {}", studentId, courseId);
        
        // Check if registration already exists
        if (registrationRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new IllegalArgumentException("Student is already registered for this course");
        }
        
        // Get student and course
        Student student = studentService.getStudentById(studentId);
        Course course = courseService.getCourseById(courseId);
        
        // Check if course has reached maximum enrollment
        if (course.getMaxEnrollment() != null) {
            long currentEnrollment = getEnrolledCountByCourse(courseId);
            if (currentEnrollment >= course.getMaxEnrollment()) {
                throw new IllegalArgumentException("Course has reached maximum enrollment capacity");
            }
        }
        
        Registration registration = new Registration();
        registration.setStudent(student);
        registration.setCourse(course);
        registration.setRemarks(remarks);
        
        return registrationRepository.save(registration);
    }
    
    @Transactional
    public Registration updateRegistration(Long id, Registration.RegistrationStatus status, String remarks) {
        log.debug("Updating registration with id: {}", id);
        
        Registration existingRegistration = getRegistrationById(id);
        existingRegistration.setStatus(status);
        existingRegistration.setRemarks(remarks);
        
        return registrationRepository.save(existingRegistration);
    }
    
    @Transactional
    public Registration updateRegistrationStatus(Long id, Registration.RegistrationStatus status) {
        log.debug("Updating registration status with id: {} to: {}", id, status);
        
        Registration existingRegistration = getRegistrationById(id);
        existingRegistration.setStatus(status);
        
        return registrationRepository.save(existingRegistration);
    }
    
    @Transactional
    public void deleteRegistration(Long id) {
        log.debug("Deleting registration with id: {}", id);
        
        if (!registrationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Registration not found with id: " + id);
        }
        
        registrationRepository.deleteById(id);
    }
    
    public long getEnrolledCountByCourse(Long courseId) {
        log.debug("Getting enrolled count for course: {}", courseId);
        return registrationRepository.countByCourseIdAndStatus(courseId, Registration.RegistrationStatus.ENROLLED);
    }
    
    public boolean isStudentRegisteredForCourse(Long studentId, Long courseId) {
        return registrationRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }
    
    public Registration getRegistrationByStudentAndCourse(Long studentId, Long courseId) {
        return registrationRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Registration not found for student: " + studentId + " and course: " + courseId));
    }
}