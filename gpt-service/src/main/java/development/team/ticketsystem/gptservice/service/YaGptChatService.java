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

    @Value("${yagpt.model:general}")
    private String model;

    private final PromptsConfiguration promptsConfiguration;

    /**
     * Улучшает описание тикета с контекстом названия и текущего описания
     * @param description Исходное описание для улучшения
     * @return Улучшенное описание
     */
    public String upgradeDescription(String description, String ticketName, String currentDescription) {
        try {
            String systemPrompt = this.promptsConfiguration.getUpgradeDescriptionPrompt(ticketName, currentDescription);
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", description));
            return configureRequestAndSend(systemPrompt, messages);
        } catch (Exception e) {
            return "Извините, сервис временно недоступен. Пожалуйста, попробуйте позже.";
        }
    }

    /**
     * Генерирует ответ в чате на основе истории и контекста тикета
     * @param userMessage Сообщение пользователя
     * @param chatHistory История чата
     * @param pointName Название тикета
     * @param pointDescription Описание тикета
     * @return Ответ ассистента
     */
    public String generateChatResponse(String userMessage,
                                       List<Map<String, String>> chatHistory,
                                       String pointName,
                                       String pointDescription) {
        try {
            List<Map<String, String>> messages = new ArrayList<>(chatHistory);
            messages.add(Map.of("role", "user", "content", userMessage));

            String systemPrompt = this.promptsConfiguration.getChatAssistantPrompt(pointName, pointDescription);

            return configureRequestAndSend(systemPrompt, messages);

        } catch (Exception e) {
            e.printStackTrace();
            return "Извините, я временно недоступен. Пожалуйста, попробуйте позже.";
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
