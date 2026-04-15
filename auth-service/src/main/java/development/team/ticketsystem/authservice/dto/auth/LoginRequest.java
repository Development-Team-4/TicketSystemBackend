package development.team.ticketsystem.authservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на авторизацию")
public class LoginRequest {

    @Schema(
            description = "Email пользователя",
            example = "user@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("userEmail")
    private String email;

    @Schema(
            description = "Пароль пользователя (минимум 8 символов)",
            example = "password123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("userPassword")
    private String password;
}
