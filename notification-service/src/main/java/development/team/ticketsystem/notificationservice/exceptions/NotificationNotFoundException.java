package development.team.ticketsystem.notificationservice.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class NotificationNotFoundException extends Exception {
    private final UUID id;
}
