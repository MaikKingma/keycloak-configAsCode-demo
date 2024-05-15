package nl.the_experts.keycloak.process.realm;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.the_experts.keycloak.domain.realm.RealmCreatedEvent;
import nl.the_experts.keycloak.domain_interaction.realm.RealmUseCase;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RealmEventHandler {

    private final RealmUseCase realmUseCase;

    public RealmEventHandler(RealmUseCase realmUseCase) {
        this.realmUseCase = realmUseCase;
    }

    @EventHandler
    @Transactional
    public void handleRealmCreatedEvent(RealmCreatedEvent event) {
        log.info("Handling RealmCreatedEvent: {}", event);
        realmUseCase.createRealmProjection(event.getId(), event.getDisplayName());
    }
}
