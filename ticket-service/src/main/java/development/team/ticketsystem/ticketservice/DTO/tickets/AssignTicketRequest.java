package development.team.ticketsystem.ticketservice.DTO.tickets;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Запрос на назначение тикета сотруднику")
public class AssignTicketRequest {
    @Schema(
            description = "ID сотрудника, которому назначается тикет",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID assigneeId;
}