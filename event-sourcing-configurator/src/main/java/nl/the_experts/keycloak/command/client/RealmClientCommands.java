package nl.the_experts.keycloak.command.client;

import nl.the_experts.keycloak.domain_interaction.client.ClientUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/realms/{realmName}/clients")
public class RealmClientCommands {

    private final ClientUseCase clientUseCase;

    public RealmClientCommands(ClientUseCase clientUseCase) {
        this.clientUseCase = clientUseCase;
    }

    @PostMapping("create")
    public void createClient(@PathVariable String realmName, @RequestBody CreateClientPayload createClientPayload) {
        clientUseCase.addClientToRealm(realmName, createClientPayload.clientId());
    }

    public record CreateClientPayload(String clientId) {
    }
}
