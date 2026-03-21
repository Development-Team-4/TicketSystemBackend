package development.team.ticketsystem.authservice.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на обновление пользователя")
public class UpdateUserRequest {

    @Schema(
            description = "Новое имя пользователя",
            example = "John Updated",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("user_name")
    private String name;

    @Schema(
            description = "Ссылка на аватар",
            example = "https://example.com/avatar.png",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("user_avatar")
    private String avatar;
}
