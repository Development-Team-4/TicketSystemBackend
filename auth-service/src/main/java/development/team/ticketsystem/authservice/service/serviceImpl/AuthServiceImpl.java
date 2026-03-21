package development.team.ticketsystem.authservice.service.serviceImpl;

import development.team.ticketsystem.authservice.dto.auth.AuthResponse;
import development.team.ticketsystem.authservice.dto.auth.LoginRequest;
import development.team.ticketsystem.authservice.dto.auth.RegisterRequest;
import development.team.ticketsystem.authservice.entity.User;
import development.team.ticketsystem.authservice.mapper.UserMapper;
import development.team.ticketsystem.authservice.repository.UserRepository;
import development.team.ticketsystem.authservice.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        log.info("Register attempt for email={}", request.email());

        if (userRepository.existsByEmail(request.email())) {
            log.warn("Registration failed: email already exists email={}", request.email());
            throw new IllegalStateException("Email already exists");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role("USER")
                .createdAt(Instant.now())
                .build();

        userRepository.save(user);

        log.info("User successfully registered email={}", request.email());

        return new AuthResponse(
                "mock-token",
                "Bearer",
                mapper.toResponse(user)
        );
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {

        log.info("Login attempt for email={}", request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Login failed: user not found email={}", request.email());
                    return new IllegalArgumentException("Invalid credentials");
                });

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            log.warn("Login failed: wrong password email={}", request.email());
            throw new IllegalArgumentException("Invalid credentials");
        }

        log.info("User successfully logged in email={}", request.email());

        return new AuthResponse(
                "mock-token",
                "Bearer",
                mapper.toResponse(user)
        );
    }
}