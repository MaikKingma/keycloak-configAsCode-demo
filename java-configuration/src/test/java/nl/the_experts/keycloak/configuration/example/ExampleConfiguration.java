package nl.the_experts.keycloak.configuration.example;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import nl.the_experts.keycloak.configuration.KeycloakConfigurationProperties;
import org.keycloak.admin.client.Keycloak;

/**
 * Configuration for Example realm.
 */
@JBossLog
@AllArgsConstructor
public class ExampleConfiguration {
    static final String REALM_NAME = "example";

    private final Keycloak keycloak;

    /**
     * Configures the example realm.
     */
    public void configure() {
        log.info("-----------------------------------------------");
        log.infof("Starting configuration of realm '%s'.", REALM_NAME);
        log.info("-----------------------------------------------");

        new RealmConfiguration(keycloak.realms(), REALM_NAME).configure();

        log.info("-----------------------------------------------");
        log.infof("Finished configuration of realm '%s'.%n", REALM_NAME);
        log.info("-----------------------------------------------");
    }
}
