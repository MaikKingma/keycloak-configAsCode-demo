package dev.kingma.keycloak.domain_interaction.client;

public interface ClientDataService {
    void createClientProjection(String realmName, String clientId);
}
