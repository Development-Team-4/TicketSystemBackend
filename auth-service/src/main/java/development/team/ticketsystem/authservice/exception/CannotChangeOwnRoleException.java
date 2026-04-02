package development.team.ticketsystem.authservice.exception;

public class CannotChangeOwnRoleException extends RuntimeException {
    public CannotChangeOwnRoleException() {
        super("Fuck, you're the admin. You're fucking cutting your own rights. Admin cannot change own role");
    }
}
