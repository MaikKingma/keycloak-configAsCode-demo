package dev.kingma.keycloak.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import dev.kingma.keycloak.configuration.eventsorcerer.EventSorcererConfiguration;
import dev.kingma.keycloak.configuration.example.ExampleConfiguration;
import org.keycloak.admin.client.Keycloak;

/**
 * Class to configure Keycloak.
 */
@JBossLog
@AllArgsConstructor
public class KeycloakConfiguration {

    private final Keycloak keycloak;

    /**
     * Starts configuration of Keycloak.
     */
    public void configure() {
        log.info("-----------------------------------------------");
        log.info("Starting Java configuration");
        log.info("-----------------------------------------------");

        new ExampleConfiguration(keycloak).configure();
        new EventSorcererConfiguration(keycloak).configure();

        log.info("-----------------------------------------------");
        log.infof("Finished Java configuration without errors.");
        log.info("-----------------------------------------------");
    }
}
