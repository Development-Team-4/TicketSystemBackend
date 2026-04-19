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

    @Schema(
            description = "Исходное описание тикета",
            example = "Проблема с входом на сайт",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(value = "originalDescription")
    private String originalDescription;

    @Schema(
            description = "Изменённое описание тикета",
            example = """
                    Проблема при попытке входа на сайт.
                    Описание:
                    * Пользователь не может авторизоваться на сайте.
                    * При попытке входа система не пропускает дальше страницы авторизации.""",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(value = "upgradedDescription")
    private String upgradedDescription;
}
