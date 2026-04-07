package development.team.ticketsystem.ticketservice.dto.categories;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Запрос на назначение сотруднику категории")
public class AssignStaffRequest {
    @Schema(
            description = "ID категории",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("staffId")
    private UUID staffId;
}
