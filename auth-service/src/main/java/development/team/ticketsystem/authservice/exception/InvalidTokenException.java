package development.team.ticketsystem.authservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ApiException {
    public InvalidTokenException() {
        super("INVALID_TOKEN", "Invalid token", HttpStatus.UNAUTHORIZED);
    }
}