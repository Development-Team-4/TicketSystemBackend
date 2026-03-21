package development.team.ticketsystem.ticketservice.DTO.topics;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Запрос на создание темы")
public class CreateTopicRequest {

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