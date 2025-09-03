package com.university.courses.service;

import com.university.courses.dto.CourseDTO;
import com.university.courses.dto.RegistrationDTO;
import com.university.courses.dto.ResultDTO;
import com.university.courses.entity.Course;
import com.university.courses.entity.Registration;
import com.university.courses.entity.Result;
import com.university.courses.entity.User;
import com.university.courses.exception.ResourceNotFoundException;
import com.university.courses.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StudentPortalService {
    
    private final UserRepository userRepository;
    private final CourseService courseService;
    private final RegistrationService registrationService;
    private final ResultService resultService;
    
    public List<CourseDTO> getAvailableCoursesForStudent() {
        User currentUser = getCurrentUser();
        if (currentUser.getStudent() == null) {
            throw new IllegalStateException("Current user is not a student");
        }
        
        List<Course> allCourses = courseService.getAllCourses();
        List<Registration> studentRegistrations = registrationService
                .getRegistrationsByStudentId(currentUser.getStudent().getId());
        
        List<Long> enrolledCourseIds = studentRegistrations.stream()
                .filter(reg -> reg.getStatus() == Registration.RegistrationStatus.ENROLLED)
                .map(reg -> reg.getCourse().getId())
                .collect(Collectors.toList());
        
        return allCourses.stream()
                .filter(course -> !enrolledCourseIds.contains(course.getId()))
                .map(this::convertCourseToDTO)
                .collect(Collectors.toList());
    }
    
    public List<CourseDTO> getEnrolledCoursesForStudent() {
        User currentUser = getCurrentUser();
        if (currentUser.getStudent() == null) {
            throw new IllegalStateException("Current user is not a student");
        }
        
        List<Registration> registrations = registrationService
                .getRegistrationsByStudentId(currentUser.getStudent().getId());
        
        return registrations.stream()
                .filter(reg -> reg.getStatus() == Registration.RegistrationStatus.ENROLLED)
                .map(reg -> convertCourseToDTO(reg.getCourse()))
                .collect(Collectors.toList());
    }
    
    public List<RegistrationDTO> getStudentRegistrations() {
        User currentUser = getCurrentUser();
        if (currentUser.getStudent() == null) {
            throw new IllegalStateException("Current user is not a student");
        }
        
        List<Registration> registrations = registrationService
                .getRegistrationsByStudentId(currentUser.getStudent().getId());
        
        return registrations.stream()
                .map(this::convertRegistrationToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ResultDTO> getStudentResults() {
        User currentUser = getCurrentUser();
        if (currentUser.getStudent() == null) {
            throw new IllegalStateException("Current user is not a student");
        }
        
        List<Result> results = resultService
                .getResultsByStudentId(currentUser.getStudent().getId());
        
        return results.stream()
                .map(this::convertResultToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public RegistrationDTO enrollInCourse(Long courseId) {
        User currentUser = getCurrentUser();
        if (currentUser.getStudent() == null) {
            throw new IllegalStateException("Current user is not a student");
        }
        
        Registration registration = registrationService.createRegistration(
                currentUser.getStudent().getId(),
                courseId,
                "Self-enrolled through student portal"
        );
        
        return convertRegistrationToDTO(registration);
    }
    
    @Transactional
    public void unenrollFromCourse(Long courseId) {
        User currentUser = getCurrentUser();
        if (currentUser.getStudent() == null) {
            throw new IllegalStateException("Current user is not a student");
        }
        
        Registration registration = registrationService.getRegistrationByStudentAndCourse(
                currentUser.getStudent().getId(),
                courseId
        );
        
        registrationService.updateRegistrationStatus(
                registration.getId(),
                Registration.RegistrationStatus.DROPPED
        );
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    private CourseDTO convertCourseToDTO(Course course) {
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
        
        long currentEnrollment = registrationService.getEnrolledCountByCourse(course.getId());
        dto.setCurrentEnrollment((int) currentEnrollment);
        
        return dto;
    }
    
    private RegistrationDTO convertRegistrationToDTO(Registration registration) {
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
    
    private ResultDTO convertResultToDTO(Result result) {
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