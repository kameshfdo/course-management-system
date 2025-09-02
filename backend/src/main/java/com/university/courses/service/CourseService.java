package com.university.courses.service;

import com.university.courses.entity.Course;
import com.university.courses.exception.ResourceNotFoundException;
import com.university.courses.repository.CourseRepository;
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
public class CourseService {
    
    private final CourseRepository courseRepository;
    
    public List<Course> getAllCourses() {
        log.debug("Fetching all courses");
        return courseRepository.findAll();
    }
    
    public Page<Course> getAllCourses(Pageable pageable) {
        log.debug("Fetching courses with pagination: {}", pageable);
        return courseRepository.findAll(pageable);
    }
    
    public Course getCourseById(Long id) {
        log.debug("Fetching course by id: {}", id);
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }
    
    public Course getCourseByCode(String code) {
        log.debug("Fetching course by code: {}", code);
        return courseRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with code: " + code));
    }
    
    @Transactional
    public Course createCourse(Course course) {
        log.debug("Creating new course: {}", course.getCode());
        
        if (courseRepository.existsByCode(course.getCode())) {
            throw new IllegalArgumentException("Course with code " + course.getCode() + " already exists");
        }
        
        return courseRepository.save(course);
    }
    
    @Transactional
    public Course updateCourse(Long id, Course updatedCourse) {
        log.debug("Updating course with id: {}", id);
        
        Course existingCourse = getCourseById(id);
        
        // Check if code is being changed and if new code already exists
        if (!existingCourse.getCode().equals(updatedCourse.getCode()) && 
            courseRepository.existsByCode(updatedCourse.getCode())) {
            throw new IllegalArgumentException("Course with code " + updatedCourse.getCode() + " already exists");
        }
        
        existingCourse.setCode(updatedCourse.getCode());
        existingCourse.setTitle(updatedCourse.getTitle());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setCredits(updatedCourse.getCredits());
        existingCourse.setDepartment(updatedCourse.getDepartment());
        existingCourse.setMaxEnrollment(updatedCourse.getMaxEnrollment());
        
        return courseRepository.save(existingCourse);
    }
    
    @Transactional
    public void deleteCourse(Long id) {
        log.debug("Deleting course with id: {}", id);
        
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        
        courseRepository.deleteById(id);
    }
    
    public List<Course> getCoursesByDepartment(String department) {
        log.debug("Fetching courses by department: {}", department);
        return courseRepository.findByDepartment(department);
    }
    
    public Page<Course> searchCourses(String title, String department, Integer minCredits, Integer maxCredits, Pageable pageable) {
        log.debug("Searching courses with filters - title: {}, department: {}, minCredits: {}, maxCredits: {}", 
                 title, department, minCredits, maxCredits);
        return courseRepository.findCoursesWithFilters(title, department, minCredits, maxCredits, pageable);
    }
    
    public List<String> getAllDepartments() {
        log.debug("Fetching all departments");
        return courseRepository.findAllDepartments();
    }
    
    public boolean existsByCode(String code) {
        return courseRepository.existsByCode(code);
    }
}
