package development.team.ticketsystem.authservice.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с настройками уведомлений")
public record NotificationSettingsResponse(

        @Schema(example = "true")
        String emailEnabled,

        @Schema(example = "false")
        String telegramEnabled

) { }
