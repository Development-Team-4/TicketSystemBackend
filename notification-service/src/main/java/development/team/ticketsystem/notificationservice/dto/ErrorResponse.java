package development.team.ticketsystem.notificationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@Schema(description = "Ошибка API")
@AllArgsConstructor
public class ErrorResponse {
    @Schema(example = "400")
    private int status;

    @Schema(example = "Некорректный формат ID пользователя")
    private String message;

    @Schema(example = "2024-01-15T10:30:00Z")
    private String timestamp;
}
