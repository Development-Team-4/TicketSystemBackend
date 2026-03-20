package development.team.ticketsystem.ticketservice.DTO.categories;

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
@Schema(description = "Ответ с данными категории")
public class CategoryResponse {

    @Schema(
            description = "Уникальный идентификатор категории",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID id;

    @Schema(
            description = "ID темы, к которой относится категория",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID topicId;

    @Schema(
            description = "Название категории",
            example = "Технические проблемы"
    )
    private String name;

    @Schema(
            description = "Описание категории",
            example = "Категория для технических вопросов и проблем"
    )
    private String description;
}