package development.team.ticketsystem.authservice.dto.user;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Данные пользователя")
public record UserResponse(

        @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(example = "John Doe")
        String name,

        @Schema(example = "john@example.com")
        String email,

        @Schema(example = "USER")
        String role,

        @Schema(example = "https://example.com/avatar.png")
        String avatar,

        @Schema(example = "2024-01-01T12:00:00Z")
        Instant createdAt
) {
    //Потом уберу. Тупо заглушка на данный момент.
    public UserResponse(){
        this(null, null,null,null,null,null);
    }
}
