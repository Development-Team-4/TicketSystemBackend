package development.team.ticketsystem.authservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на выход из системы")
public class LogoutRequest {

    @Schema(
            description = "Refresh token, который нужно отозвать",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("refreshToken")
    private String refreshToken;

    @Schema(
            description = "Access token jti, который нужно поместить в blacklist",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("accessTokenJti")
    private String accessTokenJti;
}