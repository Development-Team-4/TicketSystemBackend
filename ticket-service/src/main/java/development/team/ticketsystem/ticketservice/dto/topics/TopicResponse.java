package development.team.ticketsystem.ticketservice.dto.topics;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Ответ с данными темы")
public class TopicResponse {

    @Schema(
            description = "Уникальный идентификатор темы",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("id")
    private UUID id;

    @Schema(
            description = "Название темы",
            example = "Техническая поддержка",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("name")
    private String name;

    @Schema(
            description = "Описание темы",
            example = "Тема для вопросов по технической поддержке",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("description")
    private String description;
}