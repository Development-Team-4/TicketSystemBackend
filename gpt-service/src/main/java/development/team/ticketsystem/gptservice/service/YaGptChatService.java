package development.team.ticketsystem.gptservice.service;

import development.team.ticketsystem.gptservice.client.interfaces.LlmClient;
import development.team.ticketsystem.gptservice.configuration.PromptsConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YaGptChatService {
    private final LlmClient yaGptClient;

    @Value("${yagpt.max-tokens:5000}")
    private int maxTokens;

    @Value("${yagpt.temperature:0.7}")
    private double temperature;

    private final PromptsConfiguration promptsConfiguration;

    /**
     * Улучшает описание тикета с контекстом названия и текущего описания
     *
     * @param currentDescription Исходное описание для улучшения
     * @param ticketName         Название тикета для контекста
     * @return Улучшенное описание
     */
    public String upgradeDescription(String ticketName, String currentDescription) {
        try {

            String systemPrompt = this.promptsConfiguration.getUpgradeDescriptionPrompt(ticketName);
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", currentDescription));

            return configureRequestAndSend(systemPrompt, messages);

        } catch (Exception e) {
            return "Извините, сервис временно недоступен. Пожалуйста, попробуйте позже.";
        }
    }

    private String configureRequestAndSend(String systemPrompt, List<Map<String, String>> messages) {
        return this.yaGptClient.chatCompletion(
                systemPrompt,
                messages,
                this.maxTokens,
                this.temperature
        );
    }
}
