package dev.kingma.keycloak.command.realm;

import dev.kingma.keycloak.domain_interaction.realm.RealmFlow;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/realms")
public class RealmCommands {

    private final RealmFlow realmFlow;

    public RealmCommands(RealmFlow realmFlow) {
        this.realmFlow = realmFlow;
    }

    @PostMapping("/create")
    void createAccount(@RequestBody CreateRealmDto createRealmDto) {
        realmFlow.createRealm(createRealmDto.name, createRealmDto.displayName);
    }

    private record CreateRealmDto(String name, String displayName) {
    }
}
