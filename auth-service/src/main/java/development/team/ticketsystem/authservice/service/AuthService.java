package development.team.ticketsystem.authservice.service;

import development.team.ticketsystem.authservice.dto.auth.AuthResponse;
import development.team.ticketsystem.authservice.dto.auth.LoginRequest;
import development.team.ticketsystem.authservice.dto.auth.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
