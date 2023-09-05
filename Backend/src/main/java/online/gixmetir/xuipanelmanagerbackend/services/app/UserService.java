package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import online.gixmetir.xuipanelmanagerbackend.models.UserDto;
import online.gixmetir.xuipanelmanagerbackend.models.UserRequest;
import online.gixmetir.xuipanelmanagerbackend.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Page<UserDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(UserDto::new);
    }

    public UserDto create(UserRequest request) {
        UserEntity entity = request.toEntity();
        repository.save(entity);
        return new UserDto(entity);
    }

    public UserDto update(Long id, UserRequest request) {

        UserEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + "not found"));
        entity = request.toEntity(entity);
        repository.save(entity);
        return new UserDto(entity);
    }

    public void delete(Long id) {
        UserEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + "not found"));
        repository.delete(entity);
    }
}
