package dev.kingma.keycloak.process.client;

import lombok.extern.slf4j.Slf4j;
import dev.kingma.keycloak.domain.client.ClientEvent;
import dev.kingma.keycloak.domain_interaction.client.ClientUseCase;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@ProcessingGroup("trackingEventProcessor")
public class ClientEventHandler {

    private final ClientUseCase clientUseCase;

    public ClientEventHandler(ClientUseCase clientUseCase) {
        this.clientUseCase = clientUseCase;
    }

    @EventHandler
    @Transactional
    public void handleClientAddedEvent(ClientEvent.ClientAddedEvent clientAddedEvent) {
        log.info("Handling ClientAddedEvent: realmName={}, clientId={}", clientAddedEvent.getRealmName(),
                clientAddedEvent.getClientId());
        clientUseCase.addClientProjectionForRealm(clientAddedEvent.getRealmName(),
                clientAddedEvent.getClientId());
    }
}
