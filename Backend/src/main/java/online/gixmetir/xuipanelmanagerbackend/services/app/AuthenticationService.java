package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import online.gixmetir.xuipanelmanagerbackend.entities.AuthenticationEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import online.gixmetir.xuipanelmanagerbackend.repositories.AuthenticationRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final AuthenticationRepository repository;
private final UserRepository userRepository;
    @Autowired
    public AuthenticationService(AuthenticationRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthenticationEntity authenticationEntity = repository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("Username %s not found", username))
                );

        //        System.out.println(entity.getAuthorities().toString());
//        entity.getAuthentication();
        return userRepository.findById(authenticationEntity.getUserId()).orElseThrow(()-> new EntityNotFoundException("User not found"));
    }
}