package dev.kingma.keycloak.domain_interaction.realm;

import dev.kingma.keycloak.domain.realm.RealmCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RealmFlow {
    private final RealmService realmService;
    private final RealmDataService realmDataService;
    private final CommandGateway commandGateway;

    public RealmFlow(RealmService realmService, RealmDataService realmDataService, CommandGateway commandGateway) {
        this.realmService = realmService;
        this.realmDataService = realmDataService;
        this.commandGateway = commandGateway;
    }

    public void createRealm(String name, String displayName) {
        realmService.createRealmInKeycloak(name, displayName);
        commandGateway.send(new RealmCommand.CreateRealmCommand(name, displayName));
    }

    public void createRealmProjection(String id, String displayName) {
        realmDataService.createRealmProjection(id, displayName);
    }

    public Set<RealmDTO> getAllRealmProjections() {
        return realmDataService.findAll();
    }

    public record RealmDTO(String name, String displayName) {
    }
}
