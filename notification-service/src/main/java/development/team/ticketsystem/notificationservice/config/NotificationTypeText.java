package development.team.ticketsystem.notificationservice.config;

import development.team.ticketsystem.notificationservice.factory.YamlPropertySourceFactory;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "notifications")
@PropertySource(value = "classpath:messages.yaml", factory = YamlPropertySourceFactory.class)
@Getter
public class NotificationTypeText {
    private Map<String, Template> templates = new HashMap<>();

    @Data
    public static class Template {
        private String title;
        private String message;
    }

    public Template getTemplate(String key) {
        return this.templates.get(key);
    }
}
