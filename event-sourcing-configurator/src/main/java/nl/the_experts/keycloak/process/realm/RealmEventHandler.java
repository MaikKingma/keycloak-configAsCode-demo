package nl.the_experts.keycloak.process.realm;

import lombok.extern.slf4j.Slf4j;
import nl.the_experts.keycloak.domain.realm.RealmEvent;
import nl.the_experts.keycloak.domain_interaction.realm.RealmUseCase;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ReplayStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@ProcessingGroup("trackingEventProcessor")
public class RealmEventHandler {

    private final RealmUseCase realmUseCase;

    public RealmEventHandler(RealmUseCase realmUseCase) {
        this.realmUseCase = realmUseCase;
    }

    @EventHandler
    @Transactional
    public void handleRealmCreatedEvent(RealmEvent.RealmCreatedEvent event, ReplayStatus replayStatus) {
        if (replayStatus.isReplay()) {
            log.info("Replaying RealmCreatedEvent: {}", event);
        }
        log.info("Handling RealmCreatedEvent: {}", event);
        realmUseCase.createRealmProjection(event.getId(), event.getDisplayName());
    }
}
