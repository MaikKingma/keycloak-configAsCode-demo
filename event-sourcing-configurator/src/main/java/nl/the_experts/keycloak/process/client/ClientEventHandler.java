package nl.the_experts.keycloak.process.client;

import lombok.extern.slf4j.Slf4j;
import nl.the_experts.keycloak.domain.client.ClientAddedEvent;
import nl.the_experts.keycloak.domain_interaction.client.ClientUseCase;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ClientEventHandler {

    private final ClientUseCase clientUseCase;

    public ClientEventHandler(ClientUseCase clientUseCase) {
        this.clientUseCase = clientUseCase;
    }

    @EventHandler
    @Transactional
    public void handleClientAddedEvent(ClientAddedEvent clientAddedEvent) {
        log.info("Handling ClientAddedEvent: realmName={}, clientId={}", clientAddedEvent.getRealmName(),
                clientAddedEvent.getClientId());
        clientUseCase.addClientProjectionForRealm(clientAddedEvent.getRealmName(),
                clientAddedEvent.getClientId());
    }
}
