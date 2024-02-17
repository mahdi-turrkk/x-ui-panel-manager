package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import online.gixmetir.xuipanelmanagerbackend.entities.OsSettingEntity;
import online.gixmetir.xuipanelmanagerbackend.models.OsSettingDto;
import online.gixmetir.xuipanelmanagerbackend.models.OsSettingRequest;
import online.gixmetir.xuipanelmanagerbackend.repositories.OsSettingRepository;
import org.springframework.stereotype.Service;

@Service
public class OsSettingService {
    private final OsSettingRepository repository;

    public OsSettingService(OsSettingRepository repository) {
        this.repository = repository;
    }

    public OsSettingDto update(Long id, OsSettingRequest request) {
        OsSettingEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Global setting not found"));
        entity = request.toEntity(entity);
        repository.save(entity);
        return new OsSettingDto(entity);
    }

    public OsSettingDto save(OsSettingRequest request) {
        OsSettingEntity entity = request.toEntity();
        repository.save(entity);
        return new OsSettingDto(entity);
    }
}