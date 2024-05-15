package nl.the_experts.keycloak.domain_interaction.client;

public interface ClientService {

    void createRealmClient(String realmName, String clientId);
}
