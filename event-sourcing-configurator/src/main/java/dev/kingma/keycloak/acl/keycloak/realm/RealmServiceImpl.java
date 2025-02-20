package dev.kingma.keycloak.acl.keycloak.realm;

import dev.kingma.keycloak.acl.keycloak.KeycloakClient;
import dev.kingma.keycloak.domain_interaction.realm.RealmService;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.stereotype.Service;

@Service
public class RealmServiceImpl implements RealmService {

    private final KeycloakClient keycloakClient;

    public RealmServiceImpl(KeycloakClient keycloakClient) {
        this.keycloakClient = keycloakClient;
    }

    @Override
    public void createRealmInKeycloak(String realmName, String displayName) {
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(realmName);
        realmRepresentation.setDisplayName(displayName);
        realmRepresentation.setEnabled(Boolean.TRUE);
        realmRepresentation.setBruteForceProtected(Boolean.TRUE);

        keycloakClient.get().realms().create(realmRepresentation);
    }
}
