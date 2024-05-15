package nl.the_experts.keycloak.acl.keycloak;

import nl.the_experts.keycloak.acl.keycloak.KeycloakClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeycloakClient {

    private final Keycloak keycloak;

    public KeycloakClient(
            @Value("${keycloak.server}") String keycloakServer,
            @Value("${keycloak.user}") String keycloakUser,
            @Value("${keycloak.password}") String keycloakPassword,
            @Value("${keycloak.realm}") String keycloakRealm)
    {

        KeycloakClientBuilder keycloakClientBuilder = KeycloakClientBuilder.create(
                keycloakServer,
                keycloakUser,
                keycloakPassword,
                keycloakRealm);
        this.keycloak = keycloakClientBuilder.getClient();
    }

    public Keycloak get() {
        return keycloak;
    }
}
