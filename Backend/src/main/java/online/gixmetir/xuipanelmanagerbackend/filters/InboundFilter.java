package online.gixmetir.xuipanelmanagerbackend.filters;

import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.LinkedList;
import java.util.List;

@Builder
public record InboundFilter(
        Long id,
        Long serverId,
        Boolean generatable,
        Long idFromPanel,
        String tag,
        String port,
        String protocol,
        Long up,
        Long down,
        Long totalUsed,
        String remark,
        Boolean enable,
        String expireDate
) implements Specification<InboundEntity> {
    @Override
    public Predicate toPredicate(
            @Nonnull Root<InboundEntity> root,
            @Nonnull CriteriaQuery<?> query,
            @Nonnull CriteriaBuilder builder) {
        List<Predicate> predicates = new LinkedList<>();

        if (id != null) {
            predicates.add(builder.equal(root.get("id"), id));
        }

        return predicates.stream().reduce(builder::and).orElse(null);
    }
}
