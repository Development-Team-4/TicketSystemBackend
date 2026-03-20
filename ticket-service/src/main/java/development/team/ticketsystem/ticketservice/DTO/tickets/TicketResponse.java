package development.team.ticketsystem.ticketservice.DTO.tickets;

import development.team.ticketsystem.ticketservice.TicketStatus;
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
@Schema(description = "Ответ с данными тикета")
public class TicketResponse {

    @Schema(
            description = "Уникальный идентификатор тикета",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID id;

    @Schema(
            description = "Тема тикета",
            example = "Не работает авторизация"
    )
    private String subject;

    @Schema(
            description = "Подробное описание проблемы",
            example = "При попытке войти в систему выдает ошибку 500"
    )
    private String description;

    @Schema(
            description = "Статус тикета",
            example = "OPEN",
            allowableValues = {"OPEN", "ASSIGNED", "IN_PROGRESS", "RESOLVED", "CLOSED"}
    )
    private TicketStatus status;

    @Schema(
            description = "ID категории тикета",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID categoryId;

    @Schema(
            description = "ID создателя тикета",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID createdBy;

    @Schema(
            description = "ID сотрудника, назначенного на тикет",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID assigneeId;

    @Schema(
            description = "Дата и время создания тикета",
            example = "2024-01-15T10:30:00Z"
    )
    private Instant createdAt;

    @Schema(
            description = "Дата и время последнего обновления тикета",
            example = "2024-01-15T15:45:00Z"
    )
    private Instant updatedAt;
}