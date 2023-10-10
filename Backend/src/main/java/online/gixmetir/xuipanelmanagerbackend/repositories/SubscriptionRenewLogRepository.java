package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionRenewLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubscriptionRenewLogRepository extends JpaRepository<SubscriptionRenewLogEntity, Long>, JpaSpecificationExecutor<SubscriptionRenewLogEntity> {
}
