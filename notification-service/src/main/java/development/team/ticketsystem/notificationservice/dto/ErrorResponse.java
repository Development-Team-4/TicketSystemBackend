package development.team.ticketsystem.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "Ошибка API")
@AllArgsConstructor
@Data
public class ErrorResponse {
    @Schema(example = "400")
    @JsonProperty(value = "status")
    private int status;

    @Schema(example = "Некорректный формат ID пользователя")
    @JsonProperty(value = "message")
    private String message;

    @Schema(example = "2024-01-15T10:30:00Z")
    @JsonProperty(value = "timestamp")
    private String timestamp;
}
