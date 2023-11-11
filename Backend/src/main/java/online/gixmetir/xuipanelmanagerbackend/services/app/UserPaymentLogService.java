package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import online.gixmetir.xuipanelmanagerbackend.entities.*;
import online.gixmetir.xuipanelmanagerbackend.filters.UserFilter;
import online.gixmetir.xuipanelmanagerbackend.filters.UserPaymentLogFilter;
import online.gixmetir.xuipanelmanagerbackend.models.*;
import online.gixmetir.xuipanelmanagerbackend.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserPaymentLogService {
    private final UserPaymentLogRepository repository;

    public UserPaymentLogService(UserPaymentLogRepository repository) {

        this.repository = repository;
    }

    public Page<UserPaymentLogDto> getAll(UserPaymentLogFilter filter, Pageable pageable) {
        return repository.findAll(filter, pageable).map(UserPaymentLogDto::new);
    }

    public UserPaymentLogDto create(UserPaymentLogRequest request) throws Exception {
        return new UserPaymentLogDto(repository.save(request.toEntity()));
    }

    public UserPaymentLogDto update(Long id, UserPaymentLogRequest request) {
        UserPaymentLogEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("payment with id: " + id + "not found"));
        entity = request.toEntity(entity);
        repository.save(entity);
        return new UserPaymentLogDto(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
