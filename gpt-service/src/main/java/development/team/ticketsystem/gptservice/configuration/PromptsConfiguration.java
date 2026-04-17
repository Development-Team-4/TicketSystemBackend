package development.team.ticketsystem.gptservice.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Component
public class PromptsConfiguration {

    @Value("${yagpt.prompts.config-location:classpath:prompts.yaml}")
    private Resource promptsResource;

    private String upgradeDescriptionPromptTemplate;

    @PostConstruct
    public void loadPrompts() {
        try {
            Yaml yaml = new Yaml();
            try (InputStream inputStream = promptsResource.getInputStream()) {
                Map<String, Map<String, Object>> prompts = yaml.load(inputStream);

                Map<String, Object> upgradePrompt = prompts.get("upgrade-description");
                if (upgradePrompt != null) {
                    this.upgradeDescriptionPromptTemplate = (String) upgradePrompt.get("template");
                } else {
                    throw new IllegalStateException("upgrade-description prompt not found in YAML");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load prompts configuration", e);
        }
    }

    public String getUpgradeDescriptionPrompt(String pointName) {
        String context = "";
        if (pointName != null && !pointName.isEmpty()) {
            context += String.format("Название задачи: %s\n", pointName);
        }
        return String.format(this.upgradeDescriptionPromptTemplate, context);
    }
}
