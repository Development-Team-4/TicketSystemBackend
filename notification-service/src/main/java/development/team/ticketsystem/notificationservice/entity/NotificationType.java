package development.team.ticketsystem.notificationservice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationType {
    STATUS_CHANGE(
            "Статус тикета изменён",
            "У Вашего тикета был изменён статус"
    ),

    COMMENT(
            "Новый комментарий",
            "К Вашему тикету добавлен новый комментарий"
    ),

    ASSIGNMENT(
            "Тикет назначен",
            "Ваш тикет успешно назначен"
    );

    private final String title;
    private final String message;
}
