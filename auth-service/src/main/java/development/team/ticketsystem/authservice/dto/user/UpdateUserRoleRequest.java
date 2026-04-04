package development.team.ticketsystem.authservice.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на изменение роли пользователя")
public class UpdateUserRoleRequest {

    @Schema(
            description = "Новая роль пользователя",
            example = "SUPPORT",
            allowableValues = {"USER", "SUPPORT", "ADMIN"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("userRole")
    private String role;
}