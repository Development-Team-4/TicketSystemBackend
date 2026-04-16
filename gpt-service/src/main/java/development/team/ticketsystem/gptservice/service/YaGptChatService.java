package development.team.ticketsystem.gptservice.service;

import development.team.ticketsystem.gptservice.client.interfaces.LlmClient;
import development.team.ticketsystem.gptservice.prompts.UpgradeDescriptionPrompts;
import development.team.ticketsystem.gptservice.service.interfaces.LlmServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YaGptChatService implements LlmServiceInterface {
    private final LlmClient yaGptClient;

    @Value("${yagpt.max-tokens:5000}")
    private int maxTokens;

    @Value("${yagpt.temperature:0.7}")
    private double temperature;

    /**
     * Метод для обновления оригинального описания тикета через Gpt
     * @param originalDescription оригинальное описание тикета
     * @return обновлённое описание после отправки промпта
     */
    @Override
    public String upgradeDescription(String originalDescription) {
        return upgradeDescription(originalDescription, null, null);
    }

    /**
     * Улучшает описание тикета с контекстом названия и текущего описания
     * @param description Исходное описание для улучшения
     * @param pointName Название тикета
     * @param pointDescription Текущее описание тикета
     * @return Улучшенное описание
     */
    private String upgradeDescription(String description, String pointName, String pointDescription) {
        try {
            // Формируем системный промпт
            String systemPrompt = UpgradeDescriptionPrompts.getUpgradeDescriptionPrompt(pointName, pointDescription);

            // Формируем сообщения для чата
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", description));

            return configureResponse(systemPrompt, messages);

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
            // Добавляем сообщение пользователя к истории
            List<Map<String, String>> messages = new ArrayList<>(chatHistory);
            messages.add(Map.of("role", "user", "content", userMessage));

            // Формируем системный промпт для чата
            String systemPrompt = UpgradeDescriptionPrompts.getChatAssistantPrompt(pointName, pointDescription);

            // Выполняем запрос

            return configureResponse(systemPrompt, messages);

        } catch (Exception e) {
            return "Извините, я временно недоступен. Пожалуйста, попробуйте позже.";
        }
    }

    private String configureResponse(String systemPrompt, List<Map<String, String>> messages) {
        return this.yaGptClient.chatCompletion(
                systemPrompt,
                messages,
                this.maxTokens,
                this.temperature
        );
    }
}
