package development.team.ticketsystem.ticketservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "category_staff")
@IdClass(development.team.ticketsystem.ticketservice.Entity.CategoryStaffId.class)
public class CategoryStaffEntity {

    @Id
    @Column(name = "category_id")
    private UUID categoryId;

    @Id
    @Column(name = "user_id")
    private UUID userId;

}