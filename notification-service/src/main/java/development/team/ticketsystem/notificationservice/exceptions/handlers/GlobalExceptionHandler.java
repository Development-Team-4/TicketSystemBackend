package development.team.ticketsystem.notificationservice.exceptions.handlers;

import development.team.ticketsystem.notificationservice.dto.ErrorResponse;
import development.team.ticketsystem.notificationservice.exceptions.NoSuchNotificationException;
import development.team.ticketsystem.notificationservice.exceptions.NotificationFormatException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.sql.Timestamp;
import java.time.Instant;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotificationFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotificationFormatException(NotificationFormatException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Format incorrect", Timestamp.from(Instant.now()).toString());
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalException(HttpServerErrorException.InternalServerError e) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal error of server", Timestamp.from(Instant.now()).toString());
    }

    @ExceptionHandler(NoSuchNotificationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleInternalException(NoSuchNotificationException e) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), "No such notification by id: " + e.getId(), Timestamp.from(Instant.now()).toString());
    }
}
