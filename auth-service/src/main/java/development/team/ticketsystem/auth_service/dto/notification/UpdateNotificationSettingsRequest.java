package development.team.ticketsystem.auth_service.dto.notification;

public record UpdateNotificationSettingsRequest(
        String emailEnabled,
        String telegramEnabled
) {}
