package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundRepository extends JpaRepository<InboundEntity, Long> {
}
