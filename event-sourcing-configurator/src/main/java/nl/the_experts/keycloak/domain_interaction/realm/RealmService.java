package nl.the_experts.keycloak.domain_interaction.realm;

public interface RealmService {
    void createRealmInKeycloak(String realmName, String displayName);
}
