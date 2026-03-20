package development.team.ticketsystem.ticketservice.DTO.topics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Schema(description = "Ответ с данными темы")
public class TopicResponse {

    @Schema(
            description = "Уникальный идентификатор темы",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID id;

    @Schema(
            description = "Название темы",
            example = "Техническая поддержка"
    )
    private String name;

    @Schema(
            description = "Описание темы",
            example = "Тема для вопросов по технической поддержке"
    )
    private String description;
}