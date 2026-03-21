package development.team.ticketsystem.authservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на регистрацию")
public class RegisterRequest {

    @Schema(
            description = "Имя пользователя",
            example = "John Doe",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("user_name")
    private String name;

    @Schema(
            description = "Email пользователя",
            example = "john@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("user_email")
    private String email;

    @Schema(
            description = "Пароль пользователя",
            example = "securePassword123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("user_password")
    private String password;
}
