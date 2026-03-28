package development.team.ticketsystem.ticketservice.repository.criteria;

import development.team.ticketsystem.ticketservice.UserRole;
import development.team.ticketsystem.ticketservice.dto.filter.TicketFilter;
import development.team.ticketsystem.ticketservice.entity.TicketEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TicketCriteriaRepository {

    private final EntityManager entityManager;

    public List<TicketEntity> findAllByFilters(TicketFilter filter) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TicketEntity> query = cb.createQuery(TicketEntity.class);
        Root<TicketEntity> root = query.from(TicketEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getCategoryId() != null) {
            predicates.add(cb.equal(root.get("categoryId"), filter.getCategoryId()));
        }

        if (filter.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getCreatedAfter() != null) {
            predicates.add(cb.greaterThanOrEqualTo(
                    root.get("createdAt"), filter.getCreatedAfter()));
        }

        if (filter.getCreatedBefore() != null) {
            predicates.add(cb.lessThanOrEqualTo(
                    root.get("createdAt"), filter.getCreatedBefore()));
        }

        applyRoleFilters(filter, cb, root, predicates);

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("createdAt")));

        return entityManager.createQuery(query).getResultList();
    }

    private void applyRoleFilters(
            TicketFilter filter,
            CriteriaBuilder cb,
            Root<TicketEntity> root,
            List<Predicate> predicates
    ) {
        switch (filter.getRole()) {

            case UserRole.ADMIN -> {
                if (filter.getAssignedTo() != null) {
                    predicates.add(cb.equal(root.get("assigneeId"), filter.getAssignedTo()));
                }

                if (filter.getCreatedBy() != null) {
                    predicates.add(cb.equal(root.get("createdBy"), filter.getCreatedBy()));
                }
            }

            case UserRole.SUPPORT -> {
                predicates.add(cb.equal(root.get("assigneeId"), filter.getUserId()));

                if (filter.getCategoryIds() != null) {
                    predicates.add(root.get("categoryId").in(filter.getCategoryIds()));
                }
            }

            case UserRole.USER -> predicates.add(cb.equal(root.get("createdBy"), filter.getUserId()));
        }
    }
}