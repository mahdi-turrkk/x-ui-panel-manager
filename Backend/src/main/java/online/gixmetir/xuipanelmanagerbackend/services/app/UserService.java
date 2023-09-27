package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import online.gixmetir.xuipanelmanagerbackend.clients.models.LoginModel;
import online.gixmetir.xuipanelmanagerbackend.entities.AuthenticationEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import online.gixmetir.xuipanelmanagerbackend.filters.UserFilter;
import online.gixmetir.xuipanelmanagerbackend.models.*;
import online.gixmetir.xuipanelmanagerbackend.repositories.AuthenticationRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.UserRepository;
import online.gixmetir.xuipanelmanagerbackend.security.jwt.JwtService;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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
        if (authenticationRepository.findByUsername(request.getUsername()).orElse(null) != null) {
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


    public UserDto changeStatus(Boolean newStatus, Long id) throws Exception {
        UserEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + "not found"));
        entity.setEnabled(newStatus);
        repository.save(entity);
        return new UserDto(entity);
    }

    public void changePassword(ChangePasswordModel changePasswordModel) throws Exception {
        UserEntity entity = new Helper().getUserFromContext();
        AuthenticationEntity authenticationEntity = null;
        if (changePasswordModel.getUserId() != null && changePasswordModel.getUserId() > 0) {
            if (entity.getRole() == Role.Admin) {
                authenticationEntity = authenticationRepository.findByUserId(changePasswordModel.getUserId()).orElseThrow(() -> new EntityNotFoundException("user not found"));
            } else {
                throw new Exception("only admin can change password");
            }
        } else {
            if (encoder.matches(changePasswordModel.getOldPassword(), entity.getPassword())) {
                authenticationEntity = authenticationRepository.findByUserId(entity.getId()).orElseThrow(() -> new EntityNotFoundException("user not found"));
            } else throw new Exception("old password doesnt.");
        }
        authenticationEntity.setPassword(encoder.encode(changePasswordModel.getNewPassword()));
        authenticationRepository.save(authenticationEntity);
    }
}
