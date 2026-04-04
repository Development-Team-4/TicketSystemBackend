package development.team.ticketsystem.ticketservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "category_staff")
@IdClass(development.team.ticketsystem.ticketservice.entity.CategoryStaffId.class)
public class CategoryStaffEntity {

    @Id
    @Column(name = "category_id", nullable = false, updatable = false)
    private UUID categoryId;

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

}