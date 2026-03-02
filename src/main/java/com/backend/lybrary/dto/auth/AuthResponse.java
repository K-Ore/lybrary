package com.backend.lybrary.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String email;
    private Set<String> roles;
}
