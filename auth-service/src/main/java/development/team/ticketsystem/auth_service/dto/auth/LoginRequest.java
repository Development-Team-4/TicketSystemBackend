package development.team.ticketsystem.auth_service.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @Email
        String email,

        @NotBlank
        @Size(min = 8)
        String password

) {}
