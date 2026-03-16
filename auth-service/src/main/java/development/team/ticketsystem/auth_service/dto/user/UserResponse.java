package development.team.ticketsystem.auth_service.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(

        UUID id,
        String name,
        String email,
        String role,
        String avatar,
        Instant createdAt
) {
    //Потом уберу. Тупо заглушка на данный момент.
    public UserResponse(){
        this(null, null,null,null,null,null);
    }
}
