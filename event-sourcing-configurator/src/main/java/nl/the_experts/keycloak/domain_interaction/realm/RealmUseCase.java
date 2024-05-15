package nl.the_experts.keycloak.domain_interaction.realm;

import nl.the_experts.keycloak.domain.realm.CreateRealmCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.jpa.DomainEventEntry;
import org.springframework.stereotype.Service;

@Service
public class RealmUseCase {
    private final RealmService realmService;
    private final RealmDataService realmDataService;
    private final CommandGateway commandGateway;

    public RealmUseCase(RealmService realmService, RealmDataService realmDataService, CommandGateway commandGateway) {
        this.realmService = realmService;
        this.realmDataService = realmDataService;
        this.commandGateway = commandGateway;
    }

    public void createRealm(String name, String displayName) {
        realmService.createRealmInKeycloak(name, displayName);
        commandGateway.send(new CreateRealmCommand(name, displayName));
    }

    public void createRealmProjection(String id, String displayName) {
        realmDataService.createRealmProjection(id, displayName);
    }
}
