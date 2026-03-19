package development.team.ticketsystem.authservice.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на обновление настроек уведомлений")
public record UpdateNotificationSettingsRequest(

        @Schema(
                description = "Email уведомления включены",
                example = "true",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String emailEnabled,

        @Schema(
                description = "Telegram уведомления включены",
                example = "false",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String telegramEnabled

) {}
