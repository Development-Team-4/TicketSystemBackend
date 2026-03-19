package development.team.ticketsystem.authservice.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на авторизацию")
public record LoginRequest(

        @Schema(
                description = "Email пользователя",
                example = "user@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email,

        @Schema(
                description = "Пароль пользователя (минимум 8 символов)",
                example = "password123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String password

) {}
