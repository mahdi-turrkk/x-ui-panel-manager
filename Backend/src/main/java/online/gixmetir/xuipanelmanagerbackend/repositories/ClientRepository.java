package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    List<ClientEntity> findAllBySubscriptionId(Long subscriptionId);
    List<ClientEntity> findAllBySubscriptionIdAndSendToUser(Long subscriptionId, Boolean sendToUser);

    ClientEntity findByInboundIdAndSubscriptionId(Long id, Long id1);

    List<ClientEntity> findAllByInboundId(Long inboundId);
}
