package online.gixmetir.xuipanelmanagerbackend.filters;

import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionRenewLogEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.LinkedList;
import java.util.List;

@Builder
public record SubscriptionRenewLogFilter(
        Long id,
        Long subscriptionId,
        Boolean markAsPaid
) implements Specification<SubscriptionRenewLogEntity> {
    @Override
    public Predicate toPredicate(
            @Nonnull Root<SubscriptionRenewLogEntity> root,
            @Nonnull CriteriaQuery<?> query,
            @Nonnull CriteriaBuilder builder) {
        List<Predicate> predicates = new LinkedList<>();

        if (id != null)
            predicates.add(builder.equal(root.get("id"), id));
        if (subscriptionId != null)
            predicates.add(builder.equal(root.get("subscription_id"), subscriptionId));
        if (markAsPaid != null)
            predicates.add(builder.equal(root.get("mark_as_paid"), markAsPaid));

        return predicates.stream().reduce(builder::and).orElse(null);
    }
}
