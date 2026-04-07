package development.team.ticketsystem.authservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidNameException extends ApiException {
    public InvalidNameException() {
        super("INVALID_NAME", "Name cannot be empty or blank", HttpStatus.BAD_REQUEST);
    }
}