package development.team.ticketsystem.notificationservice.config;

import development.team.ticketsystem.notificationservice.factory.YamlPropertySourceFactory;
import development.team.ticketsystem.notificationservice.model.Template;
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
public class NotificationContentProvider {
    private Map<String, Template> templates = new HashMap<>();

    public Template getTemplate(String key) {
        return this.templates.get(key);
    }
}
