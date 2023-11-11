package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.UserPaymentLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserPaymentLogRepository extends JpaRepository<UserPaymentLogEntity, Long>, JpaSpecificationExecutor<UserPaymentLogEntity> {
    List<UserPaymentLogEntity> findAllByUserId(Long userId);

}
