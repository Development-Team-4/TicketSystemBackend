package development.team.ticketsystem.gptservice.client.interfaces;

import java.util.List;
import java.util.Map;

public interface LlmClient {
    String chatCompletion(String systemPrompt, List<Map<String, String>> messages, int maxTokens, double temperature);
}
