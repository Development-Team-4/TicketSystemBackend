package development.team.ticketsystem.iftests.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class RestConfiguration {
    public static final String BASIC_URL = "http://localhost:";

    private TicketUrls ticketUrls;
    private NotificationUrls notificationUrls;

    @Getter
    @Setter
    public static class TicketUrls {
        @Value("{tests.urls.ticket.port}")
        private String port;

        @Value("{tests.urls.ticket.common-url}")
        private String commonUrl;

        @Value("{tests.urls.ticket.create-comment}")
        private String createComment;

        @Value("{tests.urls.ticket.change-status}")
        private String changeStatus;

        @Value("{tests.urls.ticket.assignee-ticket}")
        private String assigneeTicket;

        @Value("{tests.urls.ticket.change-ticket}")
        private String createTicket;

        @Value("{tests.urls.ticket.update-ticket}")
        private String updateTicket;
    }

    @Getter
    @Setter
    public static class NotificationUrls {
        @Value("{tests.urls.notification.port}")
        private String port;

        @Value("{tests.urls.notification.common-url}")
        private String commonUrl;

        @Value("{tests.urls.notification.get-all-notifications}")
        private String getAllNotifications;
    }
}
