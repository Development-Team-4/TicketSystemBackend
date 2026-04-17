package development.team.ticketsystem.gptservice.exceptionHandling;

import development.team.ticketsystem.gptservice.dto.error.ErrorResponse;
import development.team.ticketsystem.gptservice.exception.YaGptApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(YaGptApiException.class)
    public ResponseEntity<ErrorResponse> handleYaGptApiException(
            YaGptApiException ex,
            WebRequest request
    ) {
        HttpStatus status;
        String title;

        switch (ex.getStatusCode()) {
            case 400 -> {
                status = HttpStatus.BAD_REQUEST;
                title = "Bad Request";
            }
            case 401 -> {
                status = HttpStatus.UNAUTHORIZED;
                title = "Unauthorized";
            }
            case 429 -> {
                status = HttpStatus.TOO_MANY_REQUESTS;
                title = "Too Many Requests";
            }
            case 500, 502, 503, 504 -> {
                status = HttpStatus.BAD_GATEWAY;
                title = "GPT Service Unavailable";
            }
            default -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                title = "Internal Server Error";
            }
        }

        ErrorResponse error = ErrorResponse.builder()
                .status(status.value())
                .title(title)
                .detail(ex.getMessage())
                .timestamp(Instant.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity.status(status).body(error);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            WebRequest request
    ) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Bad Request")
                .detail(ex.getMessage())
                .timestamp(Instant.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity.badRequest().body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            WebRequest request
    ) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .title("Internal Server Error")
                .detail("Внутренняя ошибка сервера. Пожалуйста, попробуйте позже.")
                .timestamp(Instant.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}