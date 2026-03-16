package development.team.ticketsystem.auth_service.dto.notification;

public record NotificationSettingsResponse(
        String emailEnabled,
        String telegramEnabled
) {
    //Потом уберу. Тупо заглушка на данный момент.
    public NotificationSettingsResponse(){
        this(null,null);
    }
}
