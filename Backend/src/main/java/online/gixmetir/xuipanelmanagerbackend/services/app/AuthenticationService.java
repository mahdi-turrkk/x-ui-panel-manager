package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.transaction.Transactional;
import online.gixmetir.xuipanelmanagerbackend.repositories.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final AuthenticationRepository repository;

    @Autowired
    public AuthenticationService(AuthenticationRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserEntity entity = repository.findByUsername(username)
//                .map(AuthenticationEntity::getUser)
//                .orElseThrow(
//                        () -> new UsernameNotFoundException(String.format("Username %s not found", username))
//                );
//        System.out.println(entity.getAuthorizations().toString());
//        entity.getAuthorities();
//
//        return entity;
        return null;
    }

}
