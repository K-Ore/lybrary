package com.backend.lybrary.service.auth;

import com.backend.lybrary.config.JwtUtil;
import com.backend.lybrary.dto.auth.AuthResponse;
import com.backend.lybrary.dto.auth.LoginRequest;
import com.backend.lybrary.dto.auth.RegisterRequest;
import com.backend.lybrary.entity.User;
import com.backend.lybrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                );

        authenticationManager.authenticate(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid email or password"));

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRoles()
        );

        return new AuthResponse(token, user.getEmail(), user.getRoles());
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of("USER"))
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRoles()
        );

        return new AuthResponse(token, user.getEmail(), user.getRoles());
    }

    public void registerAdmin(String email, String password) {

        User admin = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(Set.of("ADMIN"))
                .build();

        userRepository.save(admin);
    }
}
