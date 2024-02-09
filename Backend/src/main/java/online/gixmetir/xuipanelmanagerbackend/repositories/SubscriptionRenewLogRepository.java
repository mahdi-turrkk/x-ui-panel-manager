package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionLogEntity;
import online.gixmetir.xuipanelmanagerbackend.models.SubscriptionLogType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SubscriptionRenewLogRepository extends JpaRepository<SubscriptionLogEntity, Long>, JpaSpecificationExecutor<SubscriptionLogEntity> {
    List<SubscriptionLogEntity> findAllBySubscriptionIdInAndMarkAsPaid(List<Long> ids, boolean markAsPaid);
    @Query("SELECT count (e) FROM SubscriptionLogEntity e WHERE e.createDate >= :startOfMonth and e.logType= :logType")
    long getNumberOfRenewLastMonth(@Param("startOfMonth") LocalDateTime startOfMonth, @Param("logType") SubscriptionLogType logType);

    List<SubscriptionLogEntity> findAllBySubscriptionId(Long id);

    List<SubscriptionLogEntity> findAllBySubscriptionUserId(Long subscription_userId);
    List<SubscriptionLogEntity> findAllBySubscriptionUserIdAndMarkAsPaid(Long subscription_userId, Boolean markAsPaid);


    void deleteAllBySubscriptionId(Long id);
}
