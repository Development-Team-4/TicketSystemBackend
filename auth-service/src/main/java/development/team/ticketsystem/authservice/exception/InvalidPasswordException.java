package development.team.ticketsystem.authservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends ApiException {
    public InvalidPasswordException() {
        super("INVALID_PASSWORD", "Password must be at least 8 characters and contain letters and digits", HttpStatus.BAD_REQUEST);
    }
}