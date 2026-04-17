package development.team.ticketsystem.gptservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromptsConfiguration {
    //@Value("${yagpt.prompts.upgrade-description-prompts}")
    private String upgradeDescriptionPrompt = """
            Ты - ассистент в системе управления задачами (тикет-системе). \
            Твоя задача - улучшать описания задач (тикетов), делая их более чёткими, \
            структурированными и информативными.
            
            %s
            
            Правила улучшения описания:
            1. Сделай описание более подробным и понятным
            2. Добавь структуру (используй маркированные списки при необходимости)
            3. Выдели ключевые моменты и требования
            4. Опиши ожидаемый результат
            5. Сохрани основную суть исходного описания
            6. Используй профессиональный, но понятный язык
            7. Удали лишние или дублирующиеся фразы
            
            Пожалуйста, улучши следующее описание задачи, следуя указанным правилам.
            Верни только улучшенное описание без дополнительных комментариев.
            """;

    public String getUpgradeDescriptionPrompt(String pointName, String pointDescription) {
        String context = "";
        if (pointName != null && !pointName.isEmpty()) {
            context += String.format("Название задачи: %s\n", pointName);
        }
        if (pointDescription != null && !pointDescription.isEmpty()) {
            context += String.format("Текущее описание: %s\n", pointDescription);
        }

        return String.format(this.upgradeDescriptionPrompt, context);
    }
}
