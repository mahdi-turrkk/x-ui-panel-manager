package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface InboundRepository extends JpaRepository<InboundEntity, Long>, JpaSpecificationExecutor<InboundEntity> {
    Optional<InboundEntity> findByIdFromPanelAndServerId(Long idFromPanel, Long serverId);

    List<InboundEntity> findByServerId(Long serverId);

    List<InboundEntity> findByServerIdAndGeneratable(Long serverId, Boolean generatable);

    List<InboundEntity> findAllByGeneratable(Boolean generatable);

    List<InboundEntity> findAllByServerId(Long id);

    void deleteAllByServerIdAndIdFromPanelNotIn(Long serverId, List<Long> ids);

    List<InboundEntity> findAllByServerIdAndIdFromPanelNotIn(Long id, List<Long> list);
}
