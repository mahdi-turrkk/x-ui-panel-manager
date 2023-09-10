package online.gixmetir.xuipanelmanagerbackend.filters;

import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import online.gixmetir.xuipanelmanagerbackend.models.Role;
import org.springframework.data.jpa.domain.Specification;

import java.util.LinkedList;
import java.util.List;

@Builder
public record UserFilter(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String address,
        Role role,
        Boolean enabled
) implements Specification<UserEntity> {
    @Override
    public Predicate toPredicate(
            @Nonnull Root<UserEntity> root,
            @Nonnull CriteriaQuery<?> query,
            @Nonnull CriteriaBuilder builder) {
        List<Predicate> predicates = new LinkedList<>();

        if (id != null) {
            predicates.add(builder.equal(root.get("id"), id));
        }


        if (firstName != null) {
            predicates.add(builder.like(root.get("firstName"), firstName));
        }

        if (lastName != null) {
            predicates.add(builder.like(root.get("lastname"), lastName));
        }

        if (email != null) {
            predicates.add(builder.like(root.get("email"), email));
        }

        if (enabled != null) {
            predicates.add(builder.equal(root.get("enabled"), enabled));
        }
        if (phoneNumber != null) {
            predicates.add(builder.like(root.get("phoneNumber"), phoneNumber));
        }
        if (address != null) {
            predicates.add(builder.like(root.get("address"), address));
        }
        if (role != null) {
            predicates.add(builder.equal(root.get("role"), role));
        }
        return predicates.stream().reduce(builder::and).orElse(null);
    }
}
