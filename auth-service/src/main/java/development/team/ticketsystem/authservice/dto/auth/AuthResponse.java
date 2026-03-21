package development.team.ticketsystem.authservice.dto.auth;

import development.team.ticketsystem.authservice.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ при аутентификации")
public record AuthResponse(

        @Schema(example = "jwt.token.here")
        String accessToken,

        @Schema(example = "Bearer")
        String tokenType,

        UserResponse user
) { }
