package development.team.ticketsystem.ticketservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "ticket_audit")
public class TicketAuditEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "ticket_id", nullable = false, insertable = false, updatable = false)
    private UUID ticketId;

    @Column(name = "operation", nullable = false, insertable = false, updatable = false)
    private String operation;

    @Column(name = "old_subject", insertable = false, updatable = false)
    private String oldSubject;

    @Column(name = "new_subject", insertable = false, updatable = false)
    private String newSubject;

    @Column(name = "old_description", insertable = false, updatable = false)
    private String oldDescription;

    @Column(name = "new_description", insertable = false, updatable = false)
    private String newDescription;

    @Column(name = "old_status", insertable = false, updatable = false)
    private String oldStatus;

    @Column(name = "new_status", insertable = false, updatable = false)
    private String newStatus;

    @Column(name = "old_assignee_id", insertable = false, updatable = false)
    private UUID oldAssigneeId;

    @Column(name = "new_assignee_id", insertable = false, updatable = false)
    private UUID newAssigneeId;

    @Column(name = "changed_at", nullable = false, insertable = false, updatable = false)
    private Instant changedAt;
}