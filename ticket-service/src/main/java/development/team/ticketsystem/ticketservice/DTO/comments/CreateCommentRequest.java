package development.team.ticketsystem.ticketservice.DTO.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Запрос на создание комментария")
public class CreateCommentRequest {

    @Schema(
            description = "Содержание комментария",
            example = "Проблема воспроизводится, исправьте пожалуйста",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("content")
    private String content;
}