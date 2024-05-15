package nl.the_experts.keycloak.command.realm;

import nl.the_experts.keycloak.acl.KeycloakClient;
import nl.the_experts.keycloak.domain_interaction.realm.RealmUseCase;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/realms")
public class RealmCommands {

    private final RealmUseCase realmUseCase;

    public RealmCommands(RealmUseCase realmUseCase) {
        this.realmUseCase = realmUseCase;
    }

    @PostMapping("/create")
    void createAccount(@RequestBody CreateRealmDto createRealmDto) {
        realmUseCase.createRealm(createRealmDto.name, createRealmDto.displayName);
    }

    private record CreateRealmDto(String name, String displayName) {
    }
}
