package development.team.ticketsystem.gptservice.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import development.team.ticketsystem.gptservice.client.interfaces.LlmClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class YaGPTClient implements LlmClient {
    @Value("${yagpt.api.url}")
    private String apiUrl;

    @Value("${yagpt.api.key}")
    private String apiKey;

    @Value("${yagpt.folder.id}")
    private String folderId;

    @Value("${yagpt.model:yandexgpt}")
    private String model;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public YaGPTClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder().build();
    }

    @Override
    public String chatCompletion(String systemPrompt,
                                 List<Map<String, String>> messages,
                                 int maxTokens,
                                 double temperature) {
        try {
            Map<String, Object> requestBody = new HashMap<>();

            // modelUri - обязательное поле в формате: gpt://{folder-id}/yandexgpt
            requestBody.put("modelUri", String.format("gpt://%s/%s", folderId, model));

            // completionOptions - настройки генерации
            Map<String, Object> completionOptions = new HashMap<>();
            completionOptions.put("maxTokens", maxTokens);
            completionOptions.put("temperature", temperature);
            requestBody.put("completionOptions", completionOptions);

            // Формируем сообщения в правильном формате
            List<Map<String, String>> formattedMessages = new ArrayList<>();

            // Добавляем системный промпт
            if (systemPrompt != null && !systemPrompt.isEmpty()) {
                formattedMessages.add(Map.of("role", "system", "text", systemPrompt));
            }

            // Добавляем все сообщения из истории
            for (Map<String, String> msg : messages) {
                String role = msg.get("role");
                String content = msg.get("content");
                if (role != null && content != null) {
                    formattedMessages.add(Map.of("role", role, "text", content));
                }
            }

            requestBody.put("messages", formattedMessages);

            // Выполняем запрос
            String responseBody = restClient.post()
                    .uri(apiUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Api-Key " + apiKey)
                    .header("x-folder-id", folderId)
                    .body(requestBody)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), (request, response) -> {
                        String errorBody = "";
                        try {
                            errorBody = new String(response.getBody().readAllBytes());
                        } catch (Exception e) {
                        }
                        throw new RuntimeException("Client error: " + response.getStatusCode() + " - " + errorBody);
                    })
                    .onStatus(status -> status.is5xxServerError(), (request, response) -> {
                        throw new RuntimeException("Server error: " + response.getStatusCode());
                    })
                    .body(String.class);

            if (responseBody != null) {
                JsonNode root = objectMapper.readTree(responseBody);

                if (root.has("error")) {
                    String errorMessage = root.path("error").path("message").asText();
                    throw new RuntimeException("Yandex GPT API error: " + errorMessage);
                }

                JsonNode result = root.path("result");
                if (!result.isMissingNode()) {
                    JsonNode alternatives = result.path("alternatives");
                    if (alternatives.isArray() && alternatives.size() > 0) {
                        JsonNode message = alternatives.get(0).path("message");
                        return message.path("text").asText();
                    }
                }

                throw new RuntimeException("Invalid response structure: " + responseBody);
            } else {
                throw new RuntimeException("Empty response from Yandex GPT");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to call Yandex GPT API", e);
        }
    }
}