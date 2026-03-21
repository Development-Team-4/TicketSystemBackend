package development.team.ticketsystem.ticketservice.Entity;

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