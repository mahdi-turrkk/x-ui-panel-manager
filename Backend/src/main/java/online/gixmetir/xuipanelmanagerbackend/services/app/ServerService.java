package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.ServerEntity;
import online.gixmetir.xuipanelmanagerbackend.models.ServerDto;
import online.gixmetir.xuipanelmanagerbackend.models.ServerRequest;
import online.gixmetir.xuipanelmanagerbackend.repositories.InboundRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.ServerRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.SubscriptionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerService {
    private final ServerRepository serverRepository;
    private final InboundRepository inboundRepository;
    private final SubscriptionService subscriptionService;
    private final SubscriptionRepository subscriptionRepository;

    public ServerService(ServerRepository serverRepository, InboundRepository inboundRepository, SubscriptionService subscriptionService, SubscriptionRepository subscriptionRepository) {
        this.serverRepository = serverRepository;
        this.inboundRepository = inboundRepository;
        this.subscriptionService = subscriptionService;
        this.subscriptionRepository = subscriptionRepository;
    }

    public Page<ServerDto> getAll(Pageable pageable) {
        return serverRepository.findAll(pageable).map(ServerDto::new);
    }

    public ServerDto create(ServerRequest request) throws Exception {
        ServerEntity serverFromDb = serverRepository.findByUrl(request.getUrl()).orElse(null);
        if (serverFromDb != null) throw new Exception("Server with url: " + request.getUrl() + " already exists");
        ServerEntity entity = request.toEntity();
        entity.setStatus(true);
        serverRepository.save(entity);
        return new ServerDto(entity);
    }

    public ServerDto update(Long id, ServerRequest request) {

        ServerEntity entity = serverRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Server with id: " + id + "not found"));
        entity = request.toEntity(entity);
        serverRepository.save(entity);
        return new ServerDto(entity);
    }

    public void delete(Long id) {
        ServerEntity entity = serverRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Server with id: " + id + "not found"));
        serverRepository.delete(entity);
    }

    public ServerDto changeStatus(Boolean newStatus, Long id) {
        ServerEntity entity = serverRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Server with id:" + id + " not found"));
        entity.setStatus(newStatus);
        serverRepository.save(entity);
        if (!newStatus) {
            List<InboundEntity> inbounds = inboundRepository.findByServerId(entity.getId());
            inbounds.forEach(a -> a.setGeneratable(newStatus));
            inboundRepository.saveAll(inbounds);
        }
        return new ServerDto(entity);
    }

    public void syncSubscriptionsWithServers() throws Exception {
        subscriptionService.addOrUpdateClientsRelatedToSubscription(subscriptionRepository.findAll());
    }
}
