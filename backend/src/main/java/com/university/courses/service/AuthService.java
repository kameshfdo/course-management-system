package com.university.courses.service;

import com.university.courses.dto.AuthenticationRequest;
import com.university.courses.dto.AuthenticationResponse;
import com.university.courses.dto.RegisterRequest;
import com.university.courses.dto.UserDTO;
import com.university.courses.entity.Student;
import com.university.courses.entity.User;
import com.university.courses.exception.ResourceNotFoundException;
import com.university.courses.repository.UserRepository;
import com.university.courses.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        log.debug("Registering new user: {}", request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
        user.setEnabled(true);
        
        // If registering as student, create student record
        if (User.Role.STUDENT.equals(user.getRole())) {
            Student student = new Student();
            student.setStudentId(request.getStudentId());
            student.setFirstName(request.getFirstName());
            student.setLastName(request.getLastName());
            student.setEmail(request.getEmail());
            student.setPhoneNumber(request.getPhoneNumber());
            student.setDateOfBirth(request.getDateOfBirth());
            student.setDepartment(request.getDepartment());
            student.setEnrollmentYear(request.getEnrollmentYear());
            
            Student savedStudent = studentService.createStudent(student);
            user.setStudent(savedStudent);
        }
        
        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(savedUser);
        
        return buildAuthResponse(savedUser, jwtToken);
    }
    
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.debug("Authenticating user: {}", request.getUsername());
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        String jwtToken = jwtService.generateToken(user);
        
        return buildAuthResponse(user, jwtToken);
    }
    
    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return convertToDTO(user);
    }
    
    public boolean validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return jwtService.isTokenValid(token, userDetails);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
    
    private AuthenticationResponse buildAuthResponse(User user, String token) {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setUserId(user.getId());
        
        if (user.getStudent() != null) {
            response.setStudentId(user.getStudent().getId());
        }
        
        return response;
    }
    
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setEnabled(user.isEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        if (user.getStudent() != null) {
            dto.setStudentId(user.getStudent().getId());
            dto.setStudentName(user.getStudent().getFullName());
        }
        
        return dto;
    }
}