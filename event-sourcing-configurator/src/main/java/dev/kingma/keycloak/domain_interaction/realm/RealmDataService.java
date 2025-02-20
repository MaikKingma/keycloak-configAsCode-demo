package dev.kingma.keycloak.domain_interaction.realm;

import java.util.Set;

public interface RealmDataService {
    void createRealmProjection(String id, String displayName);

    Set<RealmFlow.RealmDTO> findAll();
}
