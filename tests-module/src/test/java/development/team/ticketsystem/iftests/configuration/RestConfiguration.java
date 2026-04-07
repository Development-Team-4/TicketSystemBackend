package development.team.ticketsystem.iftests.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "tests")
public class RestConfiguration {
    private Urls urls = new Urls();

    @Getter
    @Setter
    public static class Urls {
        private String baseUrl;
        private Gateway gateway = new Gateway();
        private Auth auth = new Auth();
        private Ticket ticket = new Ticket();
        private Notification notification = new Notification();
    }

    @Getter
    @Setter
    public static class Gateway {
        private String port;
    }

    @Getter
    @Setter
    public static class Auth {
        private String port;

        @org.springframework.boot.context.properties.bind.Name("users-url")
        private String usersUrl;

        @org.springframework.boot.context.properties.bind.Name("auth-url")
        private String authUrl;

        @org.springframework.boot.context.properties.bind.Name("login")
        private String login;

        @org.springframework.boot.context.properties.bind.Name("register")
        private String register;

        @org.springframework.boot.context.properties.bind.Name("me")
        private String me;
    }

    @Getter
    @Setter
    public static class Ticket {
        private String port;

        @org.springframework.boot.context.properties.bind.Name("common-url")
        private String commonUrl;

        @org.springframework.boot.context.properties.bind.Name("create-comment")
        private String createComment;

        @org.springframework.boot.context.properties.bind.Name("change-status")
        private String changeStatus;

        @org.springframework.boot.context.properties.bind.Name("assignee-ticket")
        private String assigneeTicket;

        @org.springframework.boot.context.properties.bind.Name("create-ticket")
        private String createTicket;

        @org.springframework.boot.context.properties.bind.Name("update-ticket")
        private String updateTicket;
    }

    @Getter
    @Setter
    public static class Notification {
        private String port;

        @org.springframework.boot.context.properties.bind.Name("common-url")
        private String commonUrl;

        @org.springframework.boot.context.properties.bind.Name("get-all-notifications")
        private String getAllNotifications;
    }
}