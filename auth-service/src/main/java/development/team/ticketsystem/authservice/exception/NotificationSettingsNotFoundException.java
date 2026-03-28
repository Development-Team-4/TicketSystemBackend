package development.team.ticketsystem.authservice.exception;

import org.springframework.http.HttpStatus;

public class NotificationSettingsNotFoundException extends ApiException {
    public NotificationSettingsNotFoundException() {
        super("SETTINGS_NOT_FOUND", "Notification settings not found", HttpStatus.NOT_FOUND);
    }
}
