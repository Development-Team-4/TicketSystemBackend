package development.team.ticketsystem.authservice.service;

import development.team.ticketsystem.authservice.dto.auth.AuthResponse;
import development.team.ticketsystem.authservice.dto.auth.LoginRequest;
import development.team.ticketsystem.authservice.dto.auth.RegisterRequest;
import development.team.ticketsystem.authservice.entity.User;
import development.team.ticketsystem.authservice.mapper.UserMapper;
import development.team.ticketsystem.authservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService{

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        log.info("Register attempt for email={}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: email already exists email={}", request.getEmail());
            throw new IllegalStateException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .createdAt(Instant.now())
                .build();

        userRepository.save(user);

        log.info("User successfully registered email={}", request.getEmail());

        return new AuthResponse(
                "mock-token",
                "Bearer",
                mapper.toResponse(user)
        );
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {

        log.info("Login attempt for email={}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: user not found email={}", request.getEmail());
                    return new IllegalArgumentException("Invalid credentials");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            log.warn("Login failed: wrong password email={}", request.getEmail());
            throw new IllegalArgumentException("Invalid credentials");
        }

        log.info("User successfully logged in email={}", request.getEmail());

        return new AuthResponse(
                "mock-token",
                "Bearer",
                mapper.toResponse(user)
        );
    }
}