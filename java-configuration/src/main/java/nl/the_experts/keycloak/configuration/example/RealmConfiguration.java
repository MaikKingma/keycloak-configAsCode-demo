package nl.the_experts.keycloak.configuration.example;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.representations.idm.RealmRepresentation;

import java.util.List;

@JBossLog
@AllArgsConstructor
public class RealmConfiguration {

    private RealmsResource realmsResource;
    private BrowserFlowConfiguration browserFlowConfiguration;

    /**
     * Configures the realm, first validates if the realm exists and if none exists, creates the realm.
     */
    public void configure(String realmName, String realmDisplayName) {
        List<RealmRepresentation> realms = realmsResource.findAll();
        if (realms.isEmpty() || realms.stream().noneMatch(realm -> realm.getId().equals(realmName))) {
            log.infof("Realm does not yet exist, creating for realm: %s", realmName);
            createRealm(realmName, realmDisplayName, realmsResource);
        }
        updateRealm(realmName);
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

    private void updateRealm(String realmName) {
        RealmResource realmResource = realmsResource.realm(realmName);

        browserFlowConfiguration.setRealmResource(realmResource);
        String browserFlowAlias = browserFlowConfiguration.createBrowserFlow();

        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setBruteForceProtected(true);
        realmRepresentation.setEnabled(true);
        realmRepresentation.setBrowserFlow(browserFlowAlias);
        realmRepresentation.setLoginTheme("config-as-code-theme");

        realmResource.update(realmRepresentation);
    }
}
