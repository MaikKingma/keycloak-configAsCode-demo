package nl.the_experts.keycloak.configuration.example;

import lombok.extern.jbosslog.JBossLog;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.representations.idm.RealmRepresentation;

import java.util.List;

@JBossLog
class RealmConfiguration {

    private static final String REALM_DISPLAY_NAME = "example";

    private final RealmsResource realmsResource;
    private final String realmName;

    RealmConfiguration(RealmsResource realmsResource, String realmName) {
        this.realmsResource = realmsResource;
        this.realmName = realmName;
    }

    /**
     * Configures the realm, first validates if the realm exists and if none exists, creates the realm.
     */
    public void configure() {
        List<RealmRepresentation> realms = realmsResource.findAll();
        if (realms.stream().noneMatch(realm -> realm.getId().equals(realmName))) {
            log.infof("Realm does not yet exist, creating for realm: %s", realmName);
            createRealm(realmName, REALM_DISPLAY_NAME, realmsResource);
        }
        updateRealm();
    }

    private void createRealm(String realmName, String displayName, RealmsResource realmsResource) {
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setDisplayName(displayName);
        realmRepresentation.setId(realmName);
        realmRepresentation.setRealm(realmName);
        realmRepresentation.setEnabled(false);

        realmsResource.create(realmRepresentation);
        log.infof("Created realm '%s'", realmName);
    }

    private void updateRealm() {
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setBruteForceProtected(true);
        realmRepresentation.setEnabled(true);

        RealmResource realmResource = realmsResource.realm(realmName);
        realmResource.update(realmRepresentation);
    }
}
