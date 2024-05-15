package nl.the_experts.keycloak.domain_interaction.client;

import nl.the_experts.keycloak.domain.client.ClientCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

@Service
public class ClientUseCase {

    private final ClientService clientService;
    private final ClientDataService clientDataService;
    private final CommandGateway commandGateway;

    public ClientUseCase(ClientService clientService, ClientDataService clientDataService, CommandGateway commandGateway) {
        this.clientService = clientService;
        this.clientDataService = clientDataService;
        this.commandGateway = commandGateway;
    }

    public void addClientToRealm(String realmName, String clientId) {
        clientService.createRealmClient(realmName, clientId);
        commandGateway.send(new ClientCommand.AddClientCommand(realmName, clientId));
    }

    public void addClientProjectionForRealm(String realmName, String clientId) {
        clientDataService.createClientProjection(realmName, clientId);
    }
}
