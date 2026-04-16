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
public class UpgradeResponseDto {
    @JsonProperty(value = "originalDescription")
    @Schema(description = "Оригинальное описание тикета")
    private String originalDescription;

    @JsonProperty(value = "upgradedDescription")
    @Schema(description = "Изменённое описание тикета")
    private String upgradedDescription;
}
