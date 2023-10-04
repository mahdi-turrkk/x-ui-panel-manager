package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import online.gixmetir.xuipanelmanagerbackend.entities.PlanEntity;
import online.gixmetir.xuipanelmanagerbackend.models.PlanDto;
import online.gixmetir.xuipanelmanagerbackend.models.PlanRequest;
import online.gixmetir.xuipanelmanagerbackend.repositories.PlanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PlanService {
    private final PlanRepository pricingRepository;


    public PlanService(PlanRepository serverRepository) {
        this.pricingRepository = serverRepository;
    }

    public Page<PlanDto> getAll(Pageable pageable) {
        Page<PlanDto> map = pricingRepository.findAll(pageable).map(PlanDto::new);
        return map;
    }

    public PlanDto create(PlanRequest request) throws Exception {
        PlanEntity entity = request.toEntity();
        pricingRepository.save(entity);
        return new PlanDto(entity);
    }

    public PlanDto update(Long id, PlanRequest request) {

        PlanEntity entity = pricingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pricing with id: " + id + "not found"));
        entity = request.toEntity(entity);
        pricingRepository.save(entity);
        return new PlanDto(entity);
    }

    public void delete(Long id) {
        PlanEntity entity = pricingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pricing with id: " + id + "not found"));
        pricingRepository.delete(entity);
    }
}
