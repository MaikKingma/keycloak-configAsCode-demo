package nl.the_experts.keycloak.configuration.example;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.admin.client.Keycloak;

/**
 * Configuration for Example realm.
 */
@JBossLog
@AllArgsConstructor
public class ExampleConfiguration {
    static final String REALM_NAME = "example";
    static final String REALM_DISPLAY_NAME = "Example Realm CaC";

    private final Keycloak keycloak;

    /**
     * Configures the example realm.
     */
    public void configure() {
        log.info("-----------------------------------------------");
        log.infof("Starting configuration of realm '%s'.", REALM_NAME);
        log.info("-----------------------------------------------");

        new RealmConfiguration(keycloak.realms()).configure(REALM_NAME, REALM_DISPLAY_NAME);

        log.info("-----------------------------------------------");
        log.infof("Finished configuration of realm '%s'.%n", REALM_NAME);
        log.info("-----------------------------------------------");
    }
}
