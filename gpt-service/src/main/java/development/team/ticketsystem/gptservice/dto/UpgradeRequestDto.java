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

    @Schema(
            description = "Название тикета",
            example = "Проблема с входом на сайт",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(value = "ticketName")
    private String ticketName;


    @Schema(
            description = "Текущее описание тикета",
            example = "Не могу залогиниться.",
            requiredMode = Schema.RequiredMode.REQUIRED
            )
    @JsonProperty(value = "currentDescription")
    private String currentDescription;
}
