package nl.the_experts.keycloak.domain_interaction.client;

public interface ClientDataService {
    void createClientProjection(String realmName, String clientId);
}
