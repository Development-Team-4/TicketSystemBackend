package development.team.ticketsystem.authservice.dto.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ с настройками уведомлений")
public class NotificationSettingsResponse{

    @Schema(
            description = "email для уведомлений",
            example = "qee@gmai.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("user_email_notification")
    private String emailEnabled;

    @Schema(
            description = "Telegram для уведомлений",
            example = "@qxtewtenjoiu",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("user_telegram_notification")
    private String telegramEnabled;
}
