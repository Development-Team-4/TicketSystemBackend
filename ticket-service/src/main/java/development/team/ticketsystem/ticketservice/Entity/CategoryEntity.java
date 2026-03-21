package development.team.ticketsystem.ticketservice.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

@Builder
@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "topic_id")
    private UUID topicId;

    private String name;
    private String description;

}