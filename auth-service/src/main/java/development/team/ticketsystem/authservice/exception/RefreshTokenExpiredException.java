package development.team.ticketsystem.authservice.exception;

import org.springframework.http.HttpStatus;

public class RefreshTokenExpiredException extends ApiException {
    public RefreshTokenExpiredException() {
        super("REFRESH_TOKEN_EXPIRED", "Refresh token expired", HttpStatus.UNAUTHORIZED);
    }
}