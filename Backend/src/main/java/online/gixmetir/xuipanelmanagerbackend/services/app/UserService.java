package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import online.gixmetir.xuipanelmanagerbackend.clients.models.LoginModel;
import online.gixmetir.xuipanelmanagerbackend.entities.AuthenticationEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import online.gixmetir.xuipanelmanagerbackend.filters.UserFilter;
import online.gixmetir.xuipanelmanagerbackend.models.AuthDto;
import online.gixmetir.xuipanelmanagerbackend.models.UserDto;
import online.gixmetir.xuipanelmanagerbackend.models.UserRequest;
import online.gixmetir.xuipanelmanagerbackend.repositories.AuthenticationRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.UserRepository;
import online.gixmetir.xuipanelmanagerbackend.security.jwt.JwtService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationRepository authenticationRepository;

    public UserService(UserRepository repository, AuthenticationService authenticationService, PasswordEncoder encoder, JwtService jwtService, AuthenticationRepository authenticationRepository) {
        this.repository = repository;
        this.authenticationService = authenticationService;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.authenticationRepository = authenticationRepository;
    }

    public Page<UserDto> getAll(UserFilter filter, Pageable pageable) {
        return repository.findAll(filter, pageable).map(UserDto::new);
    }

    public UserDto create(UserRequest request) throws Exception {
        if (authenticationService.loadUserByUsername(request.getUsername()) != null) {
            throw new Exception("User with username: " + request.getUsername() + " already exists");
        }
        UserEntity userEntity = request.toEntity();
        repository.save(userEntity);
        AuthenticationEntity authenticationEntity = AuthenticationEntity.builder()
                .password(encoder.encode(request.getPassword()))
                .username(request.getUsername())
                .userId(userEntity.getId())
                .build();
        authenticationRepository.save(authenticationEntity);
        userEntity.setAuthenticationId(authenticationEntity.getId());
        repository.save(userEntity);
        return new UserDto(userEntity);
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

    @Transactional
    public AuthDto login(LoginModel loginModel) {
        UserEntity entity = authenticationService.loadUserByUsername(loginModel.getUsername());
        if (encoder.matches(loginModel.getPassword(), entity.getPassword())) {
            return AuthDto.builder()
                    .token(jwtService.generateToken(entity))
                    .role(entity.getRole())
                    .build();

        } else {
            throw new IllegalArgumentException("username or password is wrong");
        }
    }


}
