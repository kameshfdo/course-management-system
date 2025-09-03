package com.university.courses.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    
    private String token;
    private String username;
    private String email;
    private String role;
    private Long userId;
    private Long studentId;
}