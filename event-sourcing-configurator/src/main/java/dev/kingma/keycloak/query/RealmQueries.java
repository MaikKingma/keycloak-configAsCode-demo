package dev.kingma.keycloak.query;

import dev.kingma.keycloak.domain_interaction.realm.RealmFlow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/realms")
public class RealmQueries {

    private final RealmFlow realmFlow;

    public RealmQueries(RealmFlow realmFlow) {
        this.realmFlow = realmFlow;
    }

    @GetMapping
    public Set<RealmView> getRealms() {
        Set<RealmFlow.RealmDTO> realmDTOSet = realmFlow.getAllRealmProjections();
        return realmDTOSet.stream()
            .map(realmDTO -> new RealmView(realmDTO.name(), realmDTO.displayName()))
            .collect(Collectors.toSet());
    }

    public record RealmView(String name, String displayName) {
    }
}


