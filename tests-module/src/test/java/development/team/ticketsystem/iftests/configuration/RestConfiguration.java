package development.team.ticketsystem.iftests.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class RestConfiguration {
    @Value("${tests.urls.baseUrl}")
    private String baseUrl;

    @Value("${tests.urls.auth.port}")
    private String authPort;

    @Value("${tests.urls.ticket.port}")
    private String ticketPort;

    @Value("${tests.urls.ticket.common-url}")
    private String ticketCommonUrl;

    @Value("${tests.urls.ticket.create-comment}")
    private String createCommentUrl;

    @Value("${tests.urls.ticket.change-status}")
    private String changeStatusUrl;

    @Value("${tests.urls.ticket.assignee-ticket}")
    private String assigneeTicketUrl;

    @Value("${tests.urls.ticket.create-ticket}")
    private String createTicketUrl;

    @Value("${tests.urls.ticket.update-ticket}")
    private String updateTicketUrl;

    @Value("${tests.urls.notification.port}")
    private String notificationPort;

    @Value("${tests.urls.notification.common-url}")
    private String notificationCommonUrl;

    @Value("${tests.urls.notification.get-all-notifications}")
    private String getAllNotificationsUrl;
}