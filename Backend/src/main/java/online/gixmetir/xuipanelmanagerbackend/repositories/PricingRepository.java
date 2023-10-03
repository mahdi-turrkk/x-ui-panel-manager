package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.PricingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PricingRepository extends JpaRepository<PricingEntity, Long> {
    Optional<PricingEntity> findByTotalFlowAndPeriodLength(Long totalFlow, Integer periodLength);

}
