 package online.gixmetir.xuipanelmanagerbackend.repositories;

import online.gixmetir.xuipanelmanagerbackend.entities.ServerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

 public interface ServerRepository extends JpaRepository<ServerEntity, Long> {
    Optional<ServerEntity> findByUrl(String url);

     List<ServerEntity> findAllByStatus(boolean b);
 }
