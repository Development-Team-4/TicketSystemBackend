package development.team.ticketsystem.authservice.exception;

import org.springframework.http.HttpStatus;

public class RefreshTokenRevokedException extends ApiException {
    public RefreshTokenRevokedException() {
        super("REFRESH_TOKEN_REVOKED", "Refresh token revoked", HttpStatus.UNAUTHORIZED);
    }
}