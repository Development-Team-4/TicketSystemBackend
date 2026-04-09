package development.team.ticketsystem.ticketservice.dto.filter;

import development.team.ticketsystem.ticketservice.TicketStatus;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Параметры фильтрации тикетов")
public class TicketFilterRequest {

    @Parameter(
            description = "Фильтр по ID категории. Возвращает тикеты, принадлежащие указанной категории.",
            example = "770e8400-e29b-41d4-a716-446655440001",
            required = false
    )
    private UUID categoryId;

    @Parameter(
            description = "Фильтр по ID назначенного сотрудника. Возвращает тикеты, назначенные на указанного сотрудника.",
            example = "a3e8e6e-3497-4690-bfc2-c7292e7438f3",
            required = false
    )
    private UUID assignedTo;

    @Parameter(
            description = "Фильтр по ID автора. Возвращает тикеты, созданные указанным пользователем.",
            example = "8f1e8e6e-3497-4690-bfc2-c7292e7438f1",
            required = false
    )
    private UUID createdBy;

    @Parameter(
            description = "Фильтр по статусу тикета. Возвращает тикеты с указанным статусом.",
            example = "OPEN",
            schema = @Schema(allowableValues = {"OPEN", "ASSIGNED", "IN_PROGRESS", "RESOLVED", "CLOSED"}),
            required = false
    )
    private TicketStatus status;

    @Parameter(
            description = "Фильтр по дате создания (после). Возвращает тикеты, созданные после указанной даты и времени.",
            example = "2024-01-01T00:00:00Z",
            required = false
    )
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant createdAfter;

    @Parameter(
            description = "Фильтр по дате создания (до). Возвращает тикеты, созданные до указанной даты и времени.",
            example = "2024-12-31T23:59:59Z",
            required = false
    )
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant createdBefore;
}