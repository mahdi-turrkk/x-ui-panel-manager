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
        if (serverId != null) {
            predicates.add(builder.equal(root.get("serverId"), serverId));
        }
        if (generatable != null) {
            predicates.add(builder.equal(root.get("generatable"), generatable));
        }
        if (idFromPanel != null) {
            predicates.add(builder.equal(root.get("idFromPanel"), idFromPanel));
        }
        if (tag != null) {
            predicates.add(builder.like(root.get("tag"), tag));
        }
        if (port != null) {
            predicates.add(builder.equal(root.get("port"), port));
        }
        if (protocol != null) {
            predicates.add(builder.equal(root.get("protocol"), protocol));
        }
        if (remark != null) {
            predicates.add(builder.like(root.get("remark"), remark));
        }
        if (enable != null) {
            predicates.add(builder.equal(root.get("enable"), enable));
        }
        if (expireDate != null) {
            predicates.add(builder.equal(root.get("expireDate"), expireDate));
        }

        return predicates.stream().reduce(builder::and).orElse(null);
    }
}
