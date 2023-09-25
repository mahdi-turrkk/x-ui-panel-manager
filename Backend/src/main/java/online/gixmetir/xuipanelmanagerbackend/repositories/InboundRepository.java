package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface InboundRepository extends JpaRepository<InboundEntity, Long>, JpaSpecificationExecutor<InboundEntity> {
    Optional<InboundEntity> findByIdFromPanelAndServerId(Long idFromPanel, Long serverId);

    List<InboundEntity> findByServerId(Long serverId);

    List<InboundEntity> findAllByGeneratable(Boolean generatable);

}
