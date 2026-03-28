package development.team.ticketsystem.authservice.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Стандартизированный ответ при ошибке")
public class ErrorResponse {

    @Schema(
            description = "Машинно-читаемый код ошибки",
            example = "USER_NOT_FOUND",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String code;

    @Schema(
            description = "Краткое описание ошибки",
            example = "User not found",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String message;

    @Schema(
            description = "HTTP статус ответа",
            example = "404",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private int status;

    @Schema(
            description = "URI запроса, на котором произошла ошибка",
            example = "/users/550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String path;

    @Schema(
            description = "Время возникновения ошибки в формате UTC",
            example = "2026-03-23T18:10:00Z",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant timestamp;

    @Schema(
            description = "Дополнительные детали ошибки",
            example = "[\"Field email is invalid\"]"
    )
    private List<String> details;
}