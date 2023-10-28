package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SubscriptionRenewLogRepository extends JpaRepository<SubscriptionLogEntity, Long>, JpaSpecificationExecutor<SubscriptionLogEntity> {
    List<SubscriptionLogEntity> findAllBySubscriptionIdInAndMarkAsPaid(List<Long> ids, boolean markAsPaid);

    List<SubscriptionLogEntity> findAllBySubscriptionId(Long id);
}
