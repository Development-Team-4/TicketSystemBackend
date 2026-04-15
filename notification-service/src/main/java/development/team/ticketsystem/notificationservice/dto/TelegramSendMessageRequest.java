package development.team.ticketsystem.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TelegramSendMessageRequest {

    private  Long chatId;

    private String text;
}
