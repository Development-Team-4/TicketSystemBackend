package development.team.ticketsystem.notificationservice.config;

import development.team.ticketsystem.notificationservice.entity.NotificationType;
import development.team.ticketsystem.notificationservice.factory.YamlPropertySourceFactory;
import development.team.ticketsystem.notificationservice.model.Template;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties(prefix = "notifications")
@PropertySource(value = "classpath:messages.yaml", factory = YamlPropertySourceFactory.class)
@Getter
public class NotificationContentProvider {
    private Map<String, Template> templates = new HashMap<>();

    @PostConstruct
    public void validateTemplates() {
        Set<String> definedTypes = templates.keySet();
        Set<String> expectedTypes = Arrays.stream(NotificationType.values())
             .map(Enum::name)
             .collect(Collectors.toSet());

        Set<String> missingTypes = expectedTypes.stream()
                .filter(type -> !definedTypes.contains(type))
                .collect(Collectors.toSet());

        if (!missingTypes.isEmpty()) {
            throw new IllegalStateException("Error in parsing types from configuration file to enum");
        }
    }

    public Template getTemplate(String key) {
        return this.templates.get(key);
    }
}
