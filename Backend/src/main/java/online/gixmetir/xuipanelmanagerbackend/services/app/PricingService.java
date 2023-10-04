package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import online.gixmetir.xuipanelmanagerbackend.entities.PlanEntity;
import online.gixmetir.xuipanelmanagerbackend.models.PricingDto;
import online.gixmetir.xuipanelmanagerbackend.models.PricingRequest;
import online.gixmetir.xuipanelmanagerbackend.repositories.PlanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PricingService {
    private final PlanRepository pricingRepository;


    public PricingService(PlanRepository serverRepository) {
        this.pricingRepository = serverRepository;
    }

    public Page<PricingDto> getAll(Pageable pageable) {
        Page<PricingDto> map = pricingRepository.findAll(pageable).map(PricingDto::new);
        return map;
    }

    public PricingDto create(PricingRequest request) throws Exception {
        PlanEntity entity = request.toEntity();
        pricingRepository.save(entity);
        return new PricingDto(entity);
    }

    public PricingDto update(Long id, PricingRequest request) {

        PlanEntity entity = pricingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pricing with id: " + id + "not found"));
        entity = request.toEntity(entity);
        pricingRepository.save(entity);
        return new PricingDto(entity);
    }

    public void delete(Long id) {
        PlanEntity entity = pricingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pricing with id: " + id + "not found"));
        pricingRepository.delete(entity);
    }
}
