package development.team.ticketsystem.ticketservice.dto.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Ответ с данными комментария")
public class CommentResponse {

    @Schema(
            description = "Уникальный идентификатор комментария",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("id")
    private UUID id;

    @Schema(
            description = "ID тикета, к которому относится комментарий",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("ticketId")
    private UUID ticketId;

    @Schema(
            description = "ID автора комментария",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("authorId")
    private UUID authorId;

    @Schema(
            description = "Содержание комментария",
            example = "Проблема воспроизводится, исправьте пожалуйста",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("content")
    private String content;

    @Schema(
            description = "Дата и время создания комментария",
            example = "2024-01-15T10:30:00Z",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("createdAt")
    private Instant createdAt;
}