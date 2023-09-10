package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import online.gixmetir.xuipanelmanagerbackend.clients.models.InboundModel;
import online.gixmetir.xuipanelmanagerbackend.clients.models.InboundSettingModel;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.ServerEntity;
import online.gixmetir.xuipanelmanagerbackend.models.ServerDto;
import online.gixmetir.xuipanelmanagerbackend.repositories.InboundRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.ServerRepository;
import online.gixmetir.xuipanelmanagerbackend.services.xui.PanelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InboundService {
    private final PanelService panelService;
    private final ServerRepository serverRepository;
    private final InboundRepository inboundRepository;

    public InboundService(PanelService service, ServerRepository serverRepository, InboundRepository inboundRepository) {
        this.panelService = service;
        this.serverRepository = serverRepository;
        this.inboundRepository = inboundRepository;
    }

    public void loadAllInboundsFromPanels() throws Exception {
        List<ServerEntity> servers = serverRepository.findAll();
        for (ServerEntity server : servers) {
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
    }

    private void createNewInbound(InboundModel model, Long serverId) {
        inboundRepository.save(model.toEntity(serverId));
    }

    private void updateInbound(InboundModel model, InboundEntity entity) {
        entity = model.toEntity(entity);
        inboundRepository.save(entity);
    }
}
