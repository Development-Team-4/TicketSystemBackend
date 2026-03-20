package development.team.ticketsystem.ticketservice.DTO.tickets;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Запрос на создание тикета")
public class CreateTicketRequest {

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

    @Schema(
            description = "ID категории тикета",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID categoryId;
}