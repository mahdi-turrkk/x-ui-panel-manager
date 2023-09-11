package online.gixmetir.xuipanelmanagerbackend.filters;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import online.gixmetir.xuipanelmanagerbackend.models.Role;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Builder
public record SubscriptionFilter(
        Long id,
        String Uuid,
        Boolean status,
        String title,
        Long userId
) implements Specification<SubscriptionEntity> {
    @Override
    public Predicate toPredicate(
            @Nonnull Root<SubscriptionEntity> root,
            @Nonnull CriteriaQuery<?> query,
            @Nonnull CriteriaBuilder builder) {
        List<Predicate> predicates = new LinkedList<>();

        if (id != null)
            predicates.add(builder.equal(root.get("id"), id));
        if (Uuid != null)
            predicates.add(builder.equal(root.get("uuid"), Uuid));
        if (status != null)
            predicates.add(builder.equal(root.get("status"), status));
        if (title != null)
            predicates.add(builder.equal(root.get("title"), title));
        if (userId != null)
            predicates.add(builder.equal(root.get("userId"), userId));

        return predicates.stream().reduce(builder::and).orElse(null);
    }
}
