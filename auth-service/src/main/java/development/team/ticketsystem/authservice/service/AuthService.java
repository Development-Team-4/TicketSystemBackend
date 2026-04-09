package development.team.ticketsystem.authservice.service;

import development.team.ticketsystem.authservice.dto.auth.AuthResponse;
import development.team.ticketsystem.authservice.dto.auth.LoginRequest;
import development.team.ticketsystem.authservice.dto.auth.LogoutRequest;
import development.team.ticketsystem.authservice.dto.auth.RefreshTokenRequest;
import development.team.ticketsystem.authservice.dto.auth.RegisterRequest;
import development.team.ticketsystem.authservice.entity.RefreshToken;
import development.team.ticketsystem.authservice.entity.User;
import development.team.ticketsystem.authservice.exception.EmailAlreadyExistsException;
import development.team.ticketsystem.authservice.exception.InvalidCredentialsException;
import development.team.ticketsystem.authservice.exception.UserNotFoundException;
import development.team.ticketsystem.authservice.repository.UserRepository;
import development.team.ticketsystem.authservice.validation.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistWriterService tokenBlacklistWriterService;
    private final UserValidator validator;

    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest) {
        String email = validator.normalizeEmail(request.getEmail());
        String name = validator.normalizeName(request.getName());

        validator.validateEmail(email);
        validator.validatePassword(request.getPassword());
        validator.validateName(name);

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .createdAt(Instant.now())
                .build();

        userRepository.save(user);

        return refreshTokenService.issueTokens(
                user,
                httpRequest.getRemoteAddr(),
                httpRequest.getHeader("User-Agent")
        );
    }

    @Transactional
    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        String email = validator.normalizeEmail(request.getEmail());

        validator.validateEmail(email);
        validator.validateLoginPassword(request.getPassword());

        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return refreshTokenService.issueTokens(
                user,
                httpRequest.getRemoteAddr(),
                httpRequest.getHeader("User-Agent")
        );
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken stored = refreshTokenService.getValidRefreshToken(request.getRefreshToken());

        User user = userRepository.findById(stored.getUserId())
                .orElseThrow(UserNotFoundException::new);

        return refreshTokenService.refresh(user, request.getRefreshToken());
    }

    @Transactional
    public void logout(LogoutRequest request) {
        refreshTokenService.revoke(request.getRefreshToken());

        if (request.getAccessTokenJti() != null && !request.getAccessTokenJti().isBlank()) {
            tokenBlacklistWriterService.blacklist(
                    request.getAccessTokenJti(),
                    refreshTokenService.accessBlacklistTtl()
            );
        }
    }
}