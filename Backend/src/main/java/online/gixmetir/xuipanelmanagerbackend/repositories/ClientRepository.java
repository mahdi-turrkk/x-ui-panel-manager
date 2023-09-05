package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.ClientEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    public List<ClientEntity> findAllBySubscriptionId(Long subscriptionId);
}
