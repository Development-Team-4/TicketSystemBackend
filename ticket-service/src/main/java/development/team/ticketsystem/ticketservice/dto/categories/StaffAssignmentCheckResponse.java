package development.team.ticketsystem.ticketservice.dto.categories;

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
@Schema(description = "Ответ на проверку назначения сотрудника на категорию")
public class StaffAssignmentCheckResponse {

    @Schema(
            description = "Флаг, указывающий, назначен ли сотрудник на категорию",
            example = "true",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("assigned")
    private boolean assigned;

    @Schema(
            description = "ID категории",
            example = "770e8400-e29b-41d4-a716-446655440001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("categoryId")
    private UUID categoryId;

    @Schema(
            description = "ID сотрудника",
            example = "a3e8e6e-3497-4690-bfc2-c7292e7438f3",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("staffId")
    private UUID staffId;
}