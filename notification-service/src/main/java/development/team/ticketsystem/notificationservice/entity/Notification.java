package development.team.ticketsystem.notificationservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
public class Notification {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "ticket_id", nullable = false)
    private UUID ticketId;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private NotificationType type;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "sent", columnDefinition = "default false")
    private Boolean sent;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false, columnDefinition = "default now()")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "default now()")
    private Timestamp updatedAt;
}
