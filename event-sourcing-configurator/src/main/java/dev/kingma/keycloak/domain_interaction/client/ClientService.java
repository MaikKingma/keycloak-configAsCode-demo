package dev.kingma.keycloak.domain_interaction.client;

public interface ClientService {

    void createRealmClient(String realmName, String clientId);
}
