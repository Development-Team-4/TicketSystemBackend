package development.team.ticketsystem.authservice.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException() {
        super("USER_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND);
    }
}