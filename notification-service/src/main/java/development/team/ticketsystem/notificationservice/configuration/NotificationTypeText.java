package development.team.ticketsystem.notificationservice.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("classpath:messages.yaml")
@Getter
public class NotificationTypeText {
    @Value("${notifications.status_change.title}")
    private String statusChangeTitle;

    @Value("${notifications.status_change.message}")
    private String statusChangeMessage;

    @Value("${notifications.assignee.title}")
    private String assigneeTitle;

    @Value("${notifications.assignee.message}")
    private String assigneeMessage;

    @Value("${notifications.comment.title}")
    private String commentTitle;

    @Value("${notifications.comment.message}")
    private String commentMessage;
}
