package development.team.ticketsystem.authservice.dto.user;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Данные пользователя")
public class UserResponse {

    @Schema(
            description = "Уникальный идентификатор пользователя",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("userId")
    private UUID id;

    @Schema(
            description = "Полное имя пользователя",
            example = "John Doe",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("userName")
    private String name;

    @Schema(
            description = "Email адрес пользователя",
            example = "john@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("userEmail")
    private String email;

    @Schema(
            description = "Роль пользователя в системе",
            example = "USER",
            allowableValues = {"USER", "ADMIN", "MODERATOR"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("userRole")
    private String role;

    @Schema(
            description = "URL аватара пользователя",
            example = "https://example.com/avatar.png",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("userAvatar")
    private String avatar;

    @Schema(
            description = "Дата и время регистрации",
            example = "2024-01-01T12:00:00Z",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("userCreatedAt")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;
}