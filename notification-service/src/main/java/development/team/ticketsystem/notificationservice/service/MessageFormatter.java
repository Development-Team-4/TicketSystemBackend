package development.team.ticketsystem.notificationservice.service;

import development.team.ticketsystem.notificationservice.entity.NotificationType;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageFormatter {
    private final MessageSource messageSource;

    public MessageFormatter(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public String getTitleForNotification(NotificationType type) {
        return getMessage(type.getMessageKey() + ".title");
    }

    public String getMessageForNotification(NotificationType type) {
        return getMessage(type.getMessageKey() + ".message");
    }
}
