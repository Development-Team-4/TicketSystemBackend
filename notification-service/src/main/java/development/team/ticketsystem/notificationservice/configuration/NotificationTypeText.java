package development.team.ticketsystem.notificationservice.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Getter
public class NotificationTypeText {
    @Value("${notifications.status_change.title}") //--property key должен совпадать с тем который в .yml(ИНАЧЕ НЕ ЗАПУСТИТСЯ)
    private String statusChangeTitle;

    @Value("${notifications.status_change.message}") //--property key должен совпадать с тем который в .yml(ИНАЧЕ НЕ ЗАПУСТИТСЯ)
    private String statusChangeMessage;

    @Value("${notifications.assignee.title}") //--property key должен совпадать с тем который в .yml(ИНАЧЕ НЕ ЗАПУСТИТСЯ)
    private String assigneeTitle;

    @Value("${notifications.assignee.message}") //--property key должен совпадать с тем который в .yml(ИНАЧЕ НЕ ЗАПУСТИТСЯ)
    private String assigneeMessage;

    @Value("${notifications.comment.title}") //--property key должен совпадать с тем который в .yml(ИНАЧЕ НЕ ЗАПУСТИТСЯ)
    private String commentTitle;

    @Value("${notifications.comment.message}") //--property key должен совпадать с тем который в .yml(ИНАЧЕ НЕ ЗАПУСТИТСЯ)
    private String commentMessage;
}
