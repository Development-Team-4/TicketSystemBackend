package development.team.ticketsystem.notificationservice.dto;

public record InternalNotificationSettingsResponse(
        String telegramNotification,
        String emailNotification
) {
}