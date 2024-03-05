package online.gixmetir.xuipanelmanagerbackend.services.app;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Schedule {
    private final SyncService service;

    public Schedule(SyncService service) {
        this.service = service;
    }

    //every one hour rerun this mehtos
    @Scheduled(cron = "0 0 * * * *")
    public void run() throws Exception {
        service.syncWithPanels();
        service.expiration();
    }
}
