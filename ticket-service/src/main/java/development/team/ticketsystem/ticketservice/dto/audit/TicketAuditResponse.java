package development.team.ticketsystem.ticketservice.dto.audit;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "История изменений тикета")
public class TicketAuditResponse {

    @Schema(
            description = "Уникальный идентификатор записи аудита",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("id")
    private UUID id;

    @Schema(
            description = "Идентификатор тикета, к которому относится запись",
            example = "550e8400-e29b-41d4-a716-446655440001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("ticketId")
    private UUID ticketId;

    @Schema(
            description = "Тип операции над тикетом",
            example = "UPDATE",
            allowableValues = {"CREATE", "UPDATE", "STATUS_CHANGE", "ASSIGNMENT_CHANGE"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("operation")
    private String operation;

    @Schema(
            description = "Предыдущая тема тикета",
            example = "Не работает авторизация"
    )
    @JsonProperty("oldSubject")
    private String oldSubject;

    @Schema(
            description = "Новая тема тикета",
            example = "Не работает авторизация в личном кабинете"
    )
    @JsonProperty("newSubject")
    private String newSubject;

    @Schema(
            description = "Предыдущее описание тикета",
            example = "При попытке войти в систему выдает ошибку"
    )
    @JsonProperty("oldDescription")
    private String oldDescription;

    @Schema(
            description = "Новое описание тикета",
            example = "При попытке войти в систему после ввода логина и пароля выдается ошибка 500"
    )
    @JsonProperty("newDescription")
    private String newDescription;

    @Schema(
            description = "Предыдущий статус тикета",
            example = "OPEN",
            allowableValues = {"OPEN", "ASSIGNED", "IN_PROGRESS", "RESOLVED", "CLOSED"}
    )
    @JsonProperty("oldStatus")
    private String oldStatus;

    @Schema(
            description = "Новый статус тикета",
            example = "IN_PROGRESS",
            allowableValues = {"OPEN", "ASSIGNED", "IN_PROGRESS", "RESOLVED", "CLOSED"}
    )
    @JsonProperty("newStatus")
    private String newStatus;

    @Schema(
            description = "Предыдущий идентификатор назначенного сотрудника",
            example = "8f1e8e6e-3497-4690-bfc2-c7292e7438f1"
    )
    @JsonProperty("oldAssigneeId")
    private UUID oldAssigneeId;

    @Schema(
            description = "Новый идентификатор назначенного сотрудника",
            example = "a3e8e6e-3497-4690-bfc2-c7292e7438f3"
    )
    @JsonProperty("newAssigneeId")
    private UUID newAssigneeId;

    @Schema(
            description = "Дата и время выполнения операции",
            example = "2024-01-15T10:30:00Z",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("changedAt")
    private Instant changedAt;

}