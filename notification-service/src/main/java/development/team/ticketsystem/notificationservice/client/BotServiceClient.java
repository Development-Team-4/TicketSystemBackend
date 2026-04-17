package development.team.ticketsystem.notificationservice.client;

import development.team.ticketsystem.notificationservice.dto.TelegramSendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class BotServiceClient {

    private final RestClient botRestClient;

    public void sendTelegramMessage(Long chatId, String text, String url) {
        botRestClient.post()
                .uri("/internal/telegram/send")
                .body(new TelegramSendMessageRequest(chatId, text, url))
                .retrieve()
                .toBodilessEntity();
    }
}