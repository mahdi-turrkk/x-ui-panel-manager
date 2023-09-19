package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long>, JpaSpecificationExecutor<SubscriptionEntity> {
    Optional<SubscriptionEntity> findByUuid(String uuid);

    List<SubscriptionEntity> findAllByExpireDateAfterAndStatus(LocalDateTime expireDate, Boolean status);

    List<SubscriptionEntity> findAllByStatus(Boolean status);
}
