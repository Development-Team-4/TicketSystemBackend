package development.team.ticketsystem.notificationservice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationType {
    STATUS_CHANGE(
            "notification.status"
    ),

    COMMENT(
            "notification.comment"
    ),

    ASSIGNMENT(
            "notification.assignee"
    );

    private final String messageKey;
}
