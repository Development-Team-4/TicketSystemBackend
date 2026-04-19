package development.team.ticketsystem.gptservice.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Стандартный формат ответа с ошибкой")
public class ErrorResponse {

    @Schema(
            description = "HTTP статус код ошибки",
            example = "400",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("status")
    private Integer status;

    @Schema(
            description = "Краткое название ошибки",
            example = "Bad Request",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("title")
    private String title;

    @Schema(
            description = "Детальное описание ошибки",
            example = "Current description cannot be empty",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("detail")
    private String detail;

    @Schema(
            description = "Время возникновения ошибки",
            example = "2024-01-15T10:30:00Z",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("timestamp")
    private Instant timestamp;

    @Schema(
            description = "Путь к эндпоинту",
            example = "/gpt/upgrade",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("path")
    private String path;
}
