package development.team.ticketsystem.ticketservice.entity;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class CategoryStaffId implements Serializable {

    private UUID categoryId;
    private UUID userId;

}