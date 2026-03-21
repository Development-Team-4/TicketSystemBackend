package development.team.ticketsystem.ticketservice.DTO.tickets;

import development.team.ticketsystem.ticketservice.TicketStatus;
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
@Schema(description = "Ответ с данными тикета")
public class TicketResponse {

    @Schema(
            description = "Уникальный идентификатор тикета",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID id;

    @Schema(
            description = "Тема тикета",
            example = "Не работает авторизация",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String subject;

    @Schema(
            description = "Подробное описание проблемы",
            example = "При попытке войти в систему выдает ошибку 500",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String description;

    @Schema(
            description = "Статус тикета",
            example = "OPEN",
            allowableValues = {"OPEN", "ASSIGNED", "IN_PROGRESS", "RESOLVED", "CLOSED"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private TicketStatus status;

    @Schema(
            description = "ID категории тикета",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID categoryId;

    @Schema(
            description = "ID создателя тикета",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID createdBy;

    @Schema(
            description = "ID сотрудника, назначенного на тикет",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID assigneeId;

    @Schema(
            description = "Дата и время создания тикета",
            example = "2024-01-15T10:30:00Z",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Instant createdAt;

    @Schema(
            description = "Дата и время последнего обновления тикета",
            example = "2024-01-15T15:45:00Z",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Instant updatedAt;
}