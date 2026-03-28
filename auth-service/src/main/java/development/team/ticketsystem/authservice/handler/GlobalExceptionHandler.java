package development.team.ticketsystem.authservice.handler;

import development.team.ticketsystem.authservice.dto.error.ErrorResponse;
import development.team.ticketsystem.authservice.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(
            ApiException ex,
            HttpServletRequest request
    ) {
        log.warn("Handled api exception: code={}, message={}, path={}",
                ex.getCode(), ex.getMessage(), request.getRequestURI());

        ErrorResponse response = new ErrorResponse(
                ex.getCode(),
                ex.getMessage(),
                ex.getStatus().value(),
                request.getRequestURI(),
                Instant.now(),
                List.of()
        );

        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandledException(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("Unhandled exception on path={}", request.getRequestURI(), ex);

        ErrorResponse response = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "Unexpected internal server error",
                500,
                request.getRequestURI(),
                Instant.now(),
                List.of()
        );

        return ResponseEntity.internalServerError().body(response);
    }
}
