package development.team.ticketsystem.gptservice.dto;

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
public class UpgradeRequestDto {
    @JsonProperty(value = "description")
    @Schema(description = "")
    private String description;

    @JsonProperty(value = "ticketName")
    @Schema(description = "Название тикета")
    private String ticketName;

    @JsonProperty(value = "currentDescription")
    @Schema(description = "Текущее описание тикета")
    private String currentDescription;
}
