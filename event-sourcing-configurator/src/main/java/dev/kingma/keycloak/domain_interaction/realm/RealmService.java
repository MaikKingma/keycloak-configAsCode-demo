package dev.kingma.keycloak.domain_interaction.realm;

public interface RealmService {
    void createRealmInKeycloak(String realmName, String displayName);
}
