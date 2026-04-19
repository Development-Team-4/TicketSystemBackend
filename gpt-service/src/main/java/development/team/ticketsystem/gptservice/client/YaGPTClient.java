package development.team.ticketsystem.gptservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import development.team.ticketsystem.gptservice.client.interfaces.LlmClient;
import development.team.ticketsystem.gptservice.exception.YaGptApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class YaGPTClient implements LlmClient {

    // Специфичные константы для Yandex GPT API
    private static final String ROLE_SYSTEM = "system";
    private static final String FIELD_TEXT = "text";
    private static final String FIELD_ROLE = "role";
    private static final String FIELD_MODEL_URI = "modelUri";
    private static final String FIELD_COMPLETION_OPTIONS = "completionOptions";
    private static final String FIELD_MAX_TOKENS = "maxTokens";
    private static final String FIELD_TEMPERATURE = "temperature";
    private static final String FIELD_MESSAGES = "messages";

    // Настройки из application.yaml
    private final String apiUrl;
    private final String apiKey;
    private final String folderId;
    private final String model;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public YaGPTClient(
            ObjectMapper objectMapper,
            @Value("${yagpt.api.url}") String apiUrl,
            @Value("${yagpt.api.key}") String apiKey,
            @Value("${yagpt.folder.id}") String folderId,
            @Value("${yagpt.model:yandexgpt}") String model,
            @Value("${yagpt.timeout.connect:5000}") int connectTimeout,
            @Value("${yagpt.timeout.read:30000}") int readTimeout
    ) {

        this.objectMapper = objectMapper;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.folderId = folderId;
        this.model = model;

        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        this.restClient = RestClient.builder()
                .requestFactory(factory)
                .build();

    }


    @Override
    public String chatCompletion(
            String systemPrompt,
            List<Map<String, String>> messages,
            int maxTokens,
            double temperature
    ) {
        validateInputs(messages, maxTokens, temperature);

        try {
            Map<String, Object> requestBody = buildRequestBody(systemPrompt, messages, maxTokens, temperature);
            String responseBody = executeRequest(requestBody);
            return parseResponse(responseBody);
        } catch (Exception e) {
            throw new YaGptApiException("Failed to call Yandex GPT API", e);
        }
    }

    private void validateInputs(
            List<Map<String, String>> messages,
            int maxTokens,
            double temperature
    ) {
        if (messages == null || messages.isEmpty()) {
            throw new IllegalArgumentException("Messages cannot be null or empty");
        }
        if (maxTokens <= 0 || maxTokens > 8000) {
            throw new IllegalArgumentException("maxTokens must be between 1 and 8000");
        }
        if (temperature < 0 || temperature > 2) {
            throw new IllegalArgumentException("temperature must be between 0 and 2");
        }
    }

    private Map<String, Object> buildRequestBody(
            String systemPrompt,
            List<Map<String, String>> messages,
            int maxTokens,
            double temperature
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(FIELD_MODEL_URI, String.format("gpt://%s/%s", folderId, model));

        Map<String, Object> completionOptions = new HashMap<>();
        completionOptions.put(FIELD_MAX_TOKENS, maxTokens);
        completionOptions.put(FIELD_TEMPERATURE, temperature);
        requestBody.put(FIELD_COMPLETION_OPTIONS, completionOptions);

        requestBody.put(FIELD_MESSAGES, formatMessages(systemPrompt, messages));

        return requestBody;
    }

    private List<Map<String, String>> formatMessages(
            String systemPrompt,
            List<Map<String, String>> messages
    ) {
        List<Map<String, String>> formattedMessages = new ArrayList<>();

        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            formattedMessages.add(Map.of(FIELD_ROLE, ROLE_SYSTEM, FIELD_TEXT, systemPrompt));
        }

        for (Map<String, String> msg : messages) {
            String role = msg.get("role");
            String content = msg.get("content");
            if (role != null && content != null) {
                formattedMessages.add(Map.of(FIELD_ROLE, role, FIELD_TEXT, content));
            }
        }

        return formattedMessages;
    }

    private String executeRequest(Map<String, Object> requestBody) {
        return restClient.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Api-Key " + apiKey)
                .header("x-folder-id", folderId)
                .body(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new YaGptApiException("Client error: " + res.getStatusCode(), res.getStatusCode().value());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    throw new YaGptApiException("Server error: " + res.getStatusCode(), res.getStatusCode().value());
                })
                .body(String.class);
    }

    private String parseResponse(String responseBody) throws YaGptApiException, JsonProcessingException {
        if (responseBody == null) {
            throw new YaGptApiException("Empty response from Yandex GPT", 500);
        }

        JsonNode root = objectMapper.readTree(responseBody);

        if (root.has("error")) {
            String errorMessage = root.path("error").path("message").asText();
            throw new YaGptApiException("Yandex GPT API error: " + errorMessage, 400);
        }

        JsonNode result = root.path("result");
        if (result.isMissingNode()) {
            throw new YaGptApiException("Invalid response: missing 'result' field", 500);
        }

        JsonNode alternatives = result.path("alternatives");
        if (alternatives.isEmpty()) {
            throw new YaGptApiException("Invalid response: no alternatives", 500);
        }

        JsonNode message = alternatives.get(0).path("message");
        String text = message.path("text").asText();

        if (text.isEmpty()) {
            throw new YaGptApiException("Invalid response: empty text in message", 500);
        }

        return text;
    }
}
