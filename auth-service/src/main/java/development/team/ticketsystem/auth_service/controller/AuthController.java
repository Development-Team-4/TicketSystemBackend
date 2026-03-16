package development.team.ticketsystem.auth_service.controller;

import development.team.ticketsystem.auth_service.dto.auth.AuthResponse;
import development.team.ticketsystem.auth_service.dto.auth.LoginRequest;
import development.team.ticketsystem.auth_service.dto.auth.RegisterRequest;
import development.team.ticketsystem.auth_service.dto.user.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return new AuthResponse();
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return new AuthResponse();
    }

    @PostMapping("/logout")
    public void logout() {
    }

    @GetMapping("/me")
    public UserResponse me() {
        return new UserResponse();
    }

}

