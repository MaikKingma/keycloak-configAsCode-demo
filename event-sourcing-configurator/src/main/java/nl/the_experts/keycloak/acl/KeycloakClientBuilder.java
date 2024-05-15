package nl.the_experts.keycloak.acl;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

/**
 * Create a new {@link KeycloakClient} client based on given server, username, password and realm.
 */
@JBossLog
@AllArgsConstructor(staticName = "create")
public class KeycloakClientBuilder {

    private static final String ADMIN_CLI = "admin-cli";

    private final String server;
    private final String username;
    private final String password;
    private final String realm;

    /**
     * Returns a new {@link KeycloakClient} client.
     *
     * @return New {@link KeycloakClient} client.
     */
    public Keycloak getClient() {
        log.infof("Creating Keycloak client to connect to '%s'.", server);
        return KeycloakBuilder.builder()
                .serverUrl(server)
                .realm(realm)
                .username(username)
                .password(password)
                .clientId(ADMIN_CLI)
                .build();
    }
}
