package development.team.ticketsystem.ticketservice.dto.tickets;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Запрос на обновление тикета")
public class UpdateTicketRequest {

    @Schema(
            description = "Тема тикета",
            example = "Не работает авторизация",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("subject")
    private String subject;

    @Schema(
            description = "Подробное описание проблемы",
            example = "При попытке войти в систему выдает ошибку 500",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("description")
    private String description;
}