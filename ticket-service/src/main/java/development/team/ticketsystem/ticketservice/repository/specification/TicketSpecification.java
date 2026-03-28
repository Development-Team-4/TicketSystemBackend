package development.team.ticketsystem.ticketservice.repository.specification;

import development.team.ticketsystem.ticketservice.entity.TicketEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.UUID;

// Заменено на CriteriaRepository, но пока не удалено
@Deprecated(forRemoval = true)
public class TicketSpecification {

    public static Specification<TicketEntity> filter(
            UUID categoryId,
            UUID assignedTo,
            UUID createdBy,
            String status,
            Instant createdAfter,
            Instant createdBefore
    ) {
        return Specification
                .where(byCategory(categoryId))
                .and(byAssignee(assignedTo))
                .and(byCreatedBy(createdBy))
                .and(byStatus(status))
                .and(byCreatedAfter(createdAfter))
                .and(byCreatedBefore(createdBefore));
    }

    public static Specification<TicketEntity> byCategory(UUID categoryId) {
        return (root, query, cb) ->
                categoryId == null ? null :
                        cb.equal(root.get("categoryId"), categoryId);
    }

    public static Specification<TicketEntity> byAssignee(UUID assigneeId) {
        return (root, query, cb) ->
                assigneeId == null ? null :
                        cb.equal(root.get("assigneeId"), assigneeId);
    }

    public static Specification<TicketEntity> byCreatedBy(UUID createdBy) {
        return (root, query, cb) ->
                createdBy == null ? null :
                        cb.equal(root.get("createdBy"), createdBy);
    }

    public static Specification<TicketEntity> byStatus(String status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("status"), status);
    }

    public static Specification<TicketEntity> byCreatedAfter(Instant createdAfter) {
        return (root, query, cb) ->
                createdAfter == null ? null :
                        cb.greaterThanOrEqualTo(root.get("createdAt"), createdAfter);
    }

    public static Specification<TicketEntity> byCreatedBefore(Instant createdBefore) {
        return (root, query, cb) ->
                createdBefore == null ? null :
                        cb.lessThanOrEqualTo(root.get("createdAt"), createdBefore);
    }
}