package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import online.gixmetir.xuipanelmanagerbackend.entities.GlobalSettingEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.OsSettingEntity;
import online.gixmetir.xuipanelmanagerbackend.models.GlobalSettingDto;
import online.gixmetir.xuipanelmanagerbackend.models.GlobalSettingRequest;
import online.gixmetir.xuipanelmanagerbackend.repositories.GlobalSettingRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.OsSettingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalSettingService {
    private final GlobalSettingRepository repository;
    private final OsSettingService osSettingService;
    private final OsSettingRepository osSettingRepository;

    public GlobalSettingService(GlobalSettingRepository repository, OsSettingService osSettingService, OsSettingRepository osSettingRepository) {
        this.repository = repository;
        this.osSettingService = osSettingService;
        this.osSettingRepository = osSettingRepository;
    }

    public GlobalSettingDto save(GlobalSettingRequest request) {
        GlobalSettingEntity globalSettingEntity = request.toEntity();
        repository.save(globalSettingEntity);
        List<OsSettingEntity> osSettingentities = request.getOsSettingRequests().stream().map(a -> {
            a.setGlobalSettingId(globalSettingEntity.getId());
            return a.toEntity();
        }).toList();
        osSettingRepository.saveAll(osSettingentities);
        globalSettingEntity.setOsSettingEntityList(osSettingentities);
        return new GlobalSettingDto(globalSettingEntity);
    }

    public GlobalSettingDto update(Long id, GlobalSettingRequest request) {
        GlobalSettingEntity globalSettingEntity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Global setting not found"));
        globalSettingEntity = request.toEntity(globalSettingEntity);
        repository.save(globalSettingEntity);
        request.getOsSettingRequests().forEach(a -> {
            osSettingService.update(a.getId(), a);
        });
        return new GlobalSettingDto(globalSettingEntity);
    }

    public GlobalSettingDto get() {
        GlobalSettingEntity globalSettingEntity = repository.findAll().get(0);
        globalSettingEntity.getOsSettingEntityList();
        return new GlobalSettingDto(globalSettingEntity);
    }
}