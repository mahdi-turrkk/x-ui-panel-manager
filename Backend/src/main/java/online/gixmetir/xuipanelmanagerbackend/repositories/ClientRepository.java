package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    List<ClientEntity> findAllBySubscriptionId(Long subscriptionId);

    List<ClientEntity> findAllBySubscriptionIdAndSendToUser(Long subscriptionId, Boolean sendToUser);

    ClientEntity findByInboundIdAndSubscriptionId(Long id, Long id1);

    List<ClientEntity> findAllByInboundId(Long inboundId);

    Optional<ClientEntity> findByUuid(String uuid);

    List<ClientEntity> findAllBySubscriptionIdIn(List<Long> list);

    List<ClientEntity> findAllByInboundIdIn(List<Long> list);
}
