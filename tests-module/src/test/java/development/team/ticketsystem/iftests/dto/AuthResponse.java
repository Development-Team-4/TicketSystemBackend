package development.team.ticketsystem.iftests.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("accessToken")
    private String accessToken;

    @Schema(
            description = "Тип токена для использования в Authorization header",
            example = "Bearer",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("tokenType")
    private String tokenType;

    @Schema(
            description = "Данные аутентифицированного пользователя",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("user")
    private UserResponse user;

    @Schema(
            description = "Refresh token",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("refreshToken")
    private String refreshToken;

    @Schema(
            description = "Время жизни access token в секундах",
            example = "900",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("expiresIn")
    private long expiresIn;

    @Schema(
            description = "JTI access token. Может использоваться gateway для blacklist/logout orchestration",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("accessTokenJti")
    private String accessTokenJti;
}
