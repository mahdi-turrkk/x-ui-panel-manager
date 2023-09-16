package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.AuthenticationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticationRepository extends JpaRepository<AuthenticationEntity, Long> {
    Optional<AuthenticationEntity> findByUsername(String username);

    Optional<AuthenticationEntity> findByUserId(Long userId);
}
