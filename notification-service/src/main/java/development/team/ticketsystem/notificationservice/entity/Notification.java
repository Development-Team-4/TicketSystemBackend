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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "ticket_id")
    private UUID ticketId;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private NotificationType type;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "sent")
    private Boolean sent;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Notification(UUID userId, UUID ticketId, NotificationType type) {
        this.userId = userId;
        this.ticketId = ticketId;
        this.type = type;

        this.createdAt = Timestamp.from(Instant.now());
        this.updatedAt = Timestamp.from(Instant.now());
        this.sent = true;
        this.title = type.getTitle();
        this.message = type.getMessage();
    }
}
