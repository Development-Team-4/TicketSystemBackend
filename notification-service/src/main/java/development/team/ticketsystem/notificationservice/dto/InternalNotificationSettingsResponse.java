package development.team.ticketsystem.notificationservice.dto;

public record InternalNotificationSettingsResponse(

        String userEmailNotification,

        String userTelegramNotification
) {
}