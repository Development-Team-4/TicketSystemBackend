package development.team.ticketsystem.ticketservice.DTO.Error;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Ответ в случае ошибки")
public class ErrorResponse {

    @Schema(
            description = "Статус ответа",
            example = "403",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("status")
    private int status;

    @Schema(
            description = "Название ошибки",
            example = "Forbidden",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("error")
    private String error;

    @Schema(
            description = "Описание ошибки",
            example = "Only admin can assign staff",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("message")
    private String message;

    @Schema(
            description = "Дата и время ошибки",
            example = "2024-01-15T15:45:00Z",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @Schema(
            description = "Путь, на котором возникла ошибка",
            example = "/categories/770e8400-e29b-41d4-a716-446655440001/staff/a3e8e6e-3497-4690-bfc2-c7292e7438f3",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("path")
    private String path;
}