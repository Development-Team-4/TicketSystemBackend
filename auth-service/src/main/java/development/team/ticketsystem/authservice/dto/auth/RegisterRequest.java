package development.team.ticketsystem.authservice.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на регистрацию")
public record RegisterRequest(

        @Schema(
                description = "Имя пользователя",
                example = "John Doe",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String name,

        @Schema(
                description = "Email",
                example = "john@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email,

        @Schema(
                description = "Пароль",
                example = "securePassword123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String password

) {}
