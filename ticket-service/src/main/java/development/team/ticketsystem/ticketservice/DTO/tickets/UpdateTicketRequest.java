package development.team.ticketsystem.ticketservice.DTO.tickets;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Schema(description = "Запрос на обновление тикета")
public class UpdateTicketRequest {

    @Schema(
            description = "Тема тикета",
            example = "Не работает авторизация",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String subject;

    @Schema(
            description = "Подробное описание проблемы",
            example = "При попытке войти в систему выдает ошибку 500",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String description;
}