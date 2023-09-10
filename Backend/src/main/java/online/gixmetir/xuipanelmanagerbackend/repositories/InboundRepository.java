package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InboundRepository extends JpaRepository<InboundEntity, Long> {
    public Optional<InboundEntity> findByIdFromPanelAndServerId(Long idFromPanel, Long serverId);

    public List<InboundEntity> findAllByGeneratable(Boolean generatable);
}
