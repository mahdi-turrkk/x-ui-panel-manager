package online.gixmetir.xuipanelmanagerbackend.filters;

import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import online.gixmetir.xuipanelmanagerbackend.entities.UserPaymentLogEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.LinkedList;
import java.util.List;

@Builder
public record UserPaymentLogFilter(

) implements Specification<UserPaymentLogEntity> {
    @Override
    public Predicate toPredicate(
            @Nonnull Root<UserPaymentLogEntity> root,
            @Nonnull CriteriaQuery<?> query,
            @Nonnull CriteriaBuilder builder) {
        List<Predicate> predicates = new LinkedList<>();

        return predicates.stream().reduce(builder::and).orElse(null);
    }
}
