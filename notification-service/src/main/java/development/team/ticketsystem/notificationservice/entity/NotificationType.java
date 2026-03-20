package development.team.ticketsystem.notificationservice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationType {
    STATUS_CHANGE(
            "",
            ""
    ),

    COMMENT(
            "",
            ""
    ),

    ASSIGNMENT(
            "",
            ""
    );

    private final String title;
    private final String message;
}
