package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long>, JpaSpecificationExecutor<SubscriptionEntity> {
    Optional<SubscriptionEntity> findByUuid(String uuid);

    List<SubscriptionEntity> findAllByExpireDateAfterAndStatus(LocalDateTime expireDate, Boolean status);

    @Query("select s from SubscriptionEntity s where s.status = :status and (s.expireDate <= :expireDate or s.totalFlow<=s.totalUsed)")
    List<SubscriptionEntity> getAllExpiredSubscriptions(@Param("expireDate") LocalDateTime expireDate, @Param("status") Boolean status);

    List<SubscriptionEntity> findAllByStatus(Boolean status);

    @Query("SELECT count (e) FROM SubscriptionEntity e WHERE e.createdDate >= :startOfMonth")
    long getNumberOfSubscriptionsCreatedLastMonth(@Param("startOfMonth") LocalDateTime startOfMonth);

    @Query("SELECT count (e) FROM SubscriptionEntity e ")
    long getNumberOfAllSubscriptions();

    @Query("select sum(s.upload) from SubscriptionEntity s")
    Long getTotalUpload();

    @Query("select sum(s.download) from SubscriptionEntity s")
    Long getTotalDownload();

    List<SubscriptionEntity> findAllByUserIdAndMarkAsPaid(Long userId, Boolean markAsPaid);
}
