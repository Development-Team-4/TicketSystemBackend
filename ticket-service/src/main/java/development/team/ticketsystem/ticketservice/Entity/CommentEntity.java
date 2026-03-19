package development.team.ticketsystem.ticketservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "ticket_id")
    private UUID ticketId;

    @Column(name = "author_id")
    private UUID authorId;

    private String content;

    @Column(name = "created_at")
    private Instant createdAt;

}