package development.team.ticketsystem.ticketservice.DTO.comments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Schema(description = "Ответ с данными комментария")
public class CommentResponse {

    @Schema(
            description = "Уникальный идентификатор комментария",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID id;

    @Schema(
            description = "ID тикета, к которому относится комментарий",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID ticketId;

    @Schema(
            description = "ID автора комментария",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID authorId;

    @Schema(
            description = "Содержание комментария",
            example = "Проблема воспроизводится, исправьте пожалуйста"
    )
    private String content;

    @Schema(
            description = "Дата и время создания комментария",
            example = "2024-01-15T10:30:00Z"
    )
    private Instant createdAt;
}