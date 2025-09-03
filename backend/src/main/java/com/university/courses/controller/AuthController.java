package com.university.courses.controller;

import com.university.courses.dto.AuthenticationRequest;
import com.university.courses.dto.AuthenticationResponse;
import com.university.courses.dto.RegisterRequest;
import com.university.courses.dto.UserDTO;
import com.university.courses.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        log.info("POST /api/auth/register - Registering user: {}", request.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        log.info("POST /api/auth/login - Authenticating user: {}", request.getUsername());
        return ResponseEntity.ok(authService.authenticate(request));
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        log.info("GET /api/auth/me - Getting current user");
        return ResponseEntity.ok(authService.getCurrentUser());
    }
    
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        log.info("POST /api/auth/validate - Validating token");
        return ResponseEntity.ok(authService.validateToken(token));
    }
}