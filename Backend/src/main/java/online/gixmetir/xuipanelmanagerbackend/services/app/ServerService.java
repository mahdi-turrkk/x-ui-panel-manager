package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import online.gixmetir.xuipanelmanagerbackend.entities.ServerEntity;
import online.gixmetir.xuipanelmanagerbackend.models.ServerDto;
import online.gixmetir.xuipanelmanagerbackend.models.ServerRequest;
import online.gixmetir.xuipanelmanagerbackend.repositories.ServerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ServerService {
    private final ServerRepository serverRepository;

    public ServerService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public Page<ServerDto> getAll(Pageable pageable) {
        return serverRepository.findAll(pageable).map(ServerDto::new);
    }

    public ServerDto create(ServerRequest request) {
        ServerEntity entity = request.toEntity();
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

}
