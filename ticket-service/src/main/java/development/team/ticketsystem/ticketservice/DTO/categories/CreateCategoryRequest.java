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
@Schema(description = "Запрос на создание категории")
public class CreateCategoryRequest {

    @Schema(
            description = "ID темы, к которой относится категория",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID topicId;

    @Schema(
            description = "Название категории",
            example = "Технические проблемы",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @Schema(
            description = "Описание категории",
            example = "Категория для технических вопросов и проблем",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String description;
}