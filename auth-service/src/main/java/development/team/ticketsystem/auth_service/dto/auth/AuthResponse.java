package development.team.ticketsystem.auth_service.dto.auth;

import development.team.ticketsystem.auth_service.dto.user.UserResponse;

public record AuthResponse(
        String accessToken,
        String tokenType,
        UserResponse user
) {
    //Потом уберу. Тупо заглушка на данный момент.
    public AuthResponse(){
        this(null, null, null);
    }
}
