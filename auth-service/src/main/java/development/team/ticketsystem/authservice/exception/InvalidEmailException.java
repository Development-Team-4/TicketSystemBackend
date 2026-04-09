package development.team.ticketsystem.authservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidEmailException extends ApiException {
    public InvalidEmailException() {
        super("INVALID_EMAIL", "Email is not valid", HttpStatus.BAD_REQUEST);
    }
}