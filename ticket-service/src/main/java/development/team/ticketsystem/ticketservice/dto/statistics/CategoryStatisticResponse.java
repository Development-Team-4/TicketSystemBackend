package development.team.ticketsystem.ticketservice.dto.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Статистика тикетов по категориям")
public class CategoryStatisticResponse {
    @Schema(description = "ID категории",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("categoryId")
    private UUID categoryId;

    @Schema(description = "Количество тикетов в категории",
            example = "15",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("count")
    private Long count;
}
