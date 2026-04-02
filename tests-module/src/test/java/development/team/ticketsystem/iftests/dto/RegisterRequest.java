package development.team.ticketsystem.iftests.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на регистрацию")
@Builder
public class RegisterRequest {

    @Schema(
            description = "Имя пользователя",
            example = "John Doe",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("userName")
    private String name;

    @Schema(
            description = "Email пользователя",
            example = "john@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("userEmail")
    private String email;

    @Schema(
            description = "Пароль пользователя",
            example = "securePassword123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("userPassword")
    private String password;
}
