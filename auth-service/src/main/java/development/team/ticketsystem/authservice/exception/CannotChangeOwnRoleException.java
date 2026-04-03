package development.team.ticketsystem.authservice.exception;

import org.springframework.http.HttpStatus;

public class CannotChangeOwnRoleException extends ApiException {
    public CannotChangeOwnRoleException() {
        super("CANNOT_CHANGE_OWN_ROLE", "Admin cannot change own role", HttpStatus.BAD_REQUEST);
    }
}
