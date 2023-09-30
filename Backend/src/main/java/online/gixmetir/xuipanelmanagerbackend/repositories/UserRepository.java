package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    UserEntity findByFirstName(String firstName);

    @Query("select s from UserEntity s where s.enabled=true and ((s.isIndefiniteExpirationTime=false and s.expirationDateTime < :expireDate) or (s.isIndefiniteFlow=false and  s.totalUsed >= s.totalFlow))")
    List<UserEntity> getAllExpiredUsers(@Param("expireDate") LocalDateTime expireDate);
}
