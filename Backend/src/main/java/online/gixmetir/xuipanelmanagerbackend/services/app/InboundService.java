package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import online.gixmetir.xuipanelmanagerbackend.clients.models.InboundModel;
import online.gixmetir.xuipanelmanagerbackend.entities.ClientEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.ServerEntity;
import online.gixmetir.xuipanelmanagerbackend.filters.InboundFilter;
import online.gixmetir.xuipanelmanagerbackend.models.InboundDto;
import online.gixmetir.xuipanelmanagerbackend.models.ServerDto;
import online.gixmetir.xuipanelmanagerbackend.repositories.ClientRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.InboundRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.ServerRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.SubscriptionRepository;
import online.gixmetir.xuipanelmanagerbackend.services.xui.PanelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InboundService {
    private final PanelService panelService;
    private final ServerRepository serverRepository;
    private final InboundRepository inboundRepository;
    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionService subscriptionService;
    private final ClientRepository clientRepository;

    public InboundService(PanelService service, ServerRepository serverRepository, InboundRepository inboundRepository, SubscriptionRepository subscriptionRepository, SubscriptionService subscriptionService, ClientRepository clientRepository) {
        this.panelService = service;
        this.serverRepository = serverRepository;
        this.inboundRepository = inboundRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionService = subscriptionService;
        this.clientRepository = clientRepository;
    }

    public void loadAllInboundsFromPanels() throws Exception {
        List<ServerEntity> servers = serverRepository.findAll();
        for (ServerEntity server : servers) {
            if ((server.getIsDeleted() != null && server.getIsDeleted()) || !server.getStatus()) continue;
            InboundModel[] inboundModels = panelService.loadAllInboundsFromXuiPanel(new ServerDto(server));
            for (InboundModel inbound : inboundModels) {
                InboundEntity entity = inboundRepository.findByIdFromPanelAndServerId(inbound.getId(), server.getId()).orElse(null);
                if (entity == null) {
                    createNewInbound(inbound, server.getId());
                } else {
                    updateInbound(inbound, entity);
                }
            }

        }

    }

    public void changeInboundGeneratable(Long id, Boolean generatable) throws Exception {
        InboundEntity entity = inboundRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Inbound with id: " + id + "not found"));
        entity.setGeneratable(generatable);
        inboundRepository.save(entity);
        if (generatable) {
            subscriptionService.addOrUpdateClientsRelatedToSubscription(subscriptionRepository.findAll());
        } else {
            List<ClientEntity> clientEntities = clientRepository.findAllByInboundId(entity.getId());
            clientEntities.forEach(a -> a.setSendToUser(false));
            clientRepository.saveAll(clientEntities);
        }
    }

    private void createNewInbound(InboundModel model, Long serverId) {
        InboundEntity entity = model.toEntity(serverId);
        entity.setGeneratable(false);
        inboundRepository.save(entity);
    }

    private void updateInbound(InboundModel model, InboundEntity entity) {
        entity = model.toEntity(entity);
        inboundRepository.save(entity);
    }

    public Page<InboundDto> getAll(InboundFilter filter, Pageable pageable) {
        int pageNumber = 0;
        int pageSize = Integer.MAX_VALUE;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable1 = PageRequest.of(pageNumber, pageSize, sort);

        return inboundRepository.findAll(filter, pageable1).map(InboundDto::new);
    }
}
