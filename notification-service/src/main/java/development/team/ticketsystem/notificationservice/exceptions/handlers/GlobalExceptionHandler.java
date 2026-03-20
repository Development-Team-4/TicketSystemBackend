package development.team.ticketsystem.notificationservice.exceptions.handlers;

import development.team.ticketsystem.notificationservice.exceptions.NotificationFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(NotificationFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleNotificationFormatException(NotificationFormatException e) {
        log.error("Ошибка формата уведомления: {}", e.getMessage());

        return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
    }
}
