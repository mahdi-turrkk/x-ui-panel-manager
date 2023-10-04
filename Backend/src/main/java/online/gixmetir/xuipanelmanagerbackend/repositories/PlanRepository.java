package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<PlanEntity, Long> {
    Optional<PlanEntity> findByTotalFlowAndPeriodLength(Long totalFlow, Integer periodLength);

}
