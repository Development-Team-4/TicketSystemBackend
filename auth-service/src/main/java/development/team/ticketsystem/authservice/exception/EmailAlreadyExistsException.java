package development.team.ticketsystem.authservice.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends ApiException {
    public EmailAlreadyExistsException() {
        super("EMAIL_ALREADY_EXISTS", "Email already exists", HttpStatus.CONFLICT);
    }
}