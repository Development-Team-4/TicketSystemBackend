package development.team.ticketsystem.authservice.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на обновление пользователя")
public record UpdateUserRequest(

        @Schema(
                description = "Новое имя пользователя",
                example = "John Updated"
        )
        String name,

        @Schema(
                description = "Ссылка на аватар",
                example = "https://example.com/avatar.png"
        )
        String avatar

) {}
