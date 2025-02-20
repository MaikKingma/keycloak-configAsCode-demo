package dev.kingma.keycloak.configuration.eventsorcerer;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.admin.client.Keycloak;

/**
 * Configuration for Example realm.
 */
@JBossLog
@AllArgsConstructor
public class EventSorcererConfiguration {
    static final String REALM_NAME = "event-sorcerer";
    static final String REALM_DISPLAY_NAME = "Event Sorcerer Dashboard Realm";

    private final Keycloak keycloak;

    /**
     * Configures the event sorcerer realm.
     */
    public void configure() {
        log.info("-----------------------------------------------");
        log.infof("Starting configuration of realm '%s'.", REALM_NAME);
        log.info("-----------------------------------------------");

        new RealmConfiguration(keycloak.realms()).configure(REALM_NAME, REALM_DISPLAY_NAME);
        new ClientConfiguration(keycloak.realms()).configure(REALM_NAME);
        new UserConfiguration(keycloak.realms()).configure(REALM_NAME);


        log.info("-----------------------------------------------");
        log.infof("Finished configuration of realm '%s'.%n", REALM_NAME);
        log.info("-----------------------------------------------");
    }
}
