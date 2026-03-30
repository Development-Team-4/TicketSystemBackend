package development.team.ticketsystem.authservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на обновление access token по refresh token")
public class RefreshTokenRequest {

    @Schema(
            description = "Refresh token",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("refreshToken")
    private String refreshToken;
}