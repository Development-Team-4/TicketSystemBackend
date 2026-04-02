package development.team.ticketsystem.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import development.team.ticketsystem.notificationservice.entity.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class NotificationDto {
    @JsonProperty(value = "id")
    @NotBlank
    @NotNull
    @Schema(description = "ID уведомления", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @JsonProperty(value = "userId")
    @NotBlank
    @NotNull
    @Schema(description = "ID пользователя", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID userId;

    @JsonProperty(value = "ticketId")
    @NotBlank
    @NotNull
    @Schema(description = "ID тикета", example = "550e8400-e29b-41d4-a716-446655400002")
    private UUID ticketId;

    @JsonProperty(value = "type")
    @NotNull
    @Schema(description = "Тип уведомления", example = "COMMENT")
    private NotificationType type;

    @JsonProperty(value = "title")
    @Schema(description = "Заголовок уведомления", example = "Новый комментарий")
    private String title;

    @JsonProperty(value = "message")
    @Schema(description = "Текст сообщения", example = "К Вашему тикету добавлен новый комментарий")
    private String message;

    @JsonProperty(value = "sent")
    @Schema(description = "Статус отправки", example = "true")
    private Boolean sent;

    @JsonProperty(value = "createdAt")
    @Schema(description = "Дата создания", example = "2024-01-15T10:30:00Z")
    private Timestamp createdAt;

    @JsonProperty(value = "updatedAt")
    @Schema(description = "Дата обновления", example = "2024-01-15T10:30:00Z")
    private Timestamp updatedAt;
}
