package development.team.ticketsystem.authservice.controller;

import development.team.ticketsystem.authservice.dto.auth.AuthResponse;
import development.team.ticketsystem.authservice.dto.auth.LoginRequest;
import development.team.ticketsystem.authservice.dto.auth.RegisterRequest;
import development.team.ticketsystem.authservice.dto.user.UserResponse;
import development.team.ticketsystem.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Auth", description = "Аутентификация")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Регистрация")
    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        log.info("User registration request was received by e-mail  {}", request.email());
        return authService.register(request);
    }

    @Operation(summary = "Логин")
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        log.info("A user login request was received by e-mail {}",request.email());
        return authService.login(request);
    }

    @Operation(summary = "Выход")
    @PostMapping("/logout")
    public void logout() {}

    @Operation(summary = "Текущий пользователь")
    @GetMapping("/me")
    public UserResponse me() {
        return new UserResponse();
    }

}

