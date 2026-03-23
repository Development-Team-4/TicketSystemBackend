package development.team.ticketsystem.ticketservice.DTO.tickets;

import com.fasterxml.jackson.annotation.JsonProperty;
import development.team.ticketsystem.ticketservice.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Запрос на обновление статуса тикета")
public class UpdateStatusRequest {

    @Schema(
            description = "Новый статус тикета",
            example = "IN_PROGRESS",
            allowableValues = {"OPEN", "ASSIGNED", "IN_PROGRESS", "RESOLVED", "CLOSED"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("status")
    private TicketStatus status;
}