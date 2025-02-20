package dev.kingma.keycloak.process.realm;

import lombok.extern.slf4j.Slf4j;
import dev.kingma.keycloak.domain.realm.RealmEvent;
import dev.kingma.keycloak.domain_interaction.realm.RealmFlow;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ReplayStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@ProcessingGroup("trackingEventProcessor")
public class RealmEventHandler {

    private final RealmFlow realmFlow;

    public RealmEventHandler(RealmFlow realmFlow) {
        this.realmFlow = realmFlow;
    }

    @EventHandler
    @Transactional
    public void handleRealmCreatedEvent(RealmEvent.RealmCreatedEvent event, ReplayStatus replayStatus) {
        if (replayStatus.isReplay()) {
            log.info("Replaying RealmCreatedEvent: {}", event);
        }
        log.info("Handling RealmCreatedEvent: {}", event);
        realmFlow.createRealmProjection(event.getId(), event.getDisplayName());
    }
}
