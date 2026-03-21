package development.team.ticketsystem.ticketservice.Entity;

import development.team.ticketsystem.ticketservice.TicketStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Builder
@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class TicketEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String subject;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Column(name = "category_id")
    private UUID categoryId;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "assignee_id")
    private UUID assigneeId;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}