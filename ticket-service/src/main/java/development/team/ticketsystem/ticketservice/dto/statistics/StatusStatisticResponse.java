package development.team.ticketsystem.ticketservice.dto.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Статистика тикетов по статусам")
public class StatusStatisticResponse {
    @Schema(description = "Статус тикета",
            example = "OPEN",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("status")
    private String status;

    @Schema(description = "Количество тикетов с данным статусом",
            example = "42",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("count")
    private Long count;
}
