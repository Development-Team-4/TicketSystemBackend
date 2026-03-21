package development.team.ticketsystem.authservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import development.team.ticketsystem.authservice.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ при аутентификации")
public class AuthResponse {

    @Schema(
            description = "JWT токен доступа для авторизации",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("access_token")
    private String accessToken;

    @Schema(
            description = "Тип токена для использования в Authorization header",
            example = "Bearer",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("token_type")
    private String tokenType;

    @Schema(
            description = "Данные аутентифицированного пользователя",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("user")
    private UserResponse user;
}
