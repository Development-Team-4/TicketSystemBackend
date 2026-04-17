package development.team.ticketsystem.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequest {

    private Long chatId;

    private String text;

    private String url;
}
