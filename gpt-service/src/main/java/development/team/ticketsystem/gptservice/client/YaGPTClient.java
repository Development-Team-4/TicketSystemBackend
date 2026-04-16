package development.team.ticketsystem.gptservice.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import development.team.ticketsystem.gptservice.client.interfaces.LlmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class YaGPTClient implements LlmClient {
    @Value("${yagpt.api.url:https://llm.api.cloud.yandex.net/foundationModels/v1/completion}")
    private String apiUrl;

    @Value("${yagpt.api.key}")
    private String apiKey;

    @Value("${yagpt.folder.id}")
    private String folderId;

    @Value("${yagpt.model:general}")
    private String model;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    /**
     * Выполняет запрос к Yandex GPT API для чат-комплишена
     * @param systemPrompt Системный промпт
     * @param messages Список сообщений
     * @param maxTokens Максимальное количество токенов
     * @param temperature Температура генерации
     * @return Ответ от модели
     */
    @Override
    public String chatCompletion(String systemPrompt,
                                 List<Map<String, String>> messages,
                                 int maxTokens,
                                 double temperature) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("max_tokens", maxTokens);
            requestBody.put("temperature", temperature);

            List<Map<String, String>> formattedMessages = new ArrayList<>();

            // Добавляем системный промпт
            formattedMessages.add(Map.of("role", "system", "content", systemPrompt));

            // Добавляем все сообщения из истории
            formattedMessages.addAll(messages);

            requestBody.put("messages", formattedMessages);

            // Настраиваем заголовки
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Api-Key " + apiKey);
            headers.set("x-folder-id", folderId);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Выполняем запрос
            String responseBody = this.restClient.post()
                    .uri(apiUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Api-Key " + apiKey)
                    .header("x-folder-id", folderId)
                    .body(requestBody)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), (request, response) -> {
                        throw new RuntimeException("Client error: " + response.getStatusCode());
                    })
                    .onStatus(status -> status.is5xxServerError(), (request, response) -> {
                        throw new RuntimeException("Server error: " + response.getStatusCode());
                    })
                    .body(String.class);

            if (responseBody != null) {
                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode result = root.path("result");
                JsonNode alternative = result.path("alternatives").get(0);

                return alternative.path("message").path("content").asText();
            } else {
                throw new RuntimeException("Empty response from Yandex GPT");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to call Yandex GPT API", e);
        }
    }
}
