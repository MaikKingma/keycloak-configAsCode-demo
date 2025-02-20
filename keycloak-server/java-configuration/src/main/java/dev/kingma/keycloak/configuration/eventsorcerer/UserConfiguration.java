package dev.kingma.keycloak.configuration.eventsorcerer;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Locale;

@JBossLog
@AllArgsConstructor
public class UserConfiguration {

    private final RealmsResource realmsResource;

    public void configure(String realmName) {
        log.infof("Starting user configuration of realm '%s'.", realmName);

        RealmResource realmResource = realmsResource.realm(realmName);
        UsersResource usersResource = realmResource.users();

        createNewUserIfNotExists(usersResource, "event-sorcerer", "event-sorcerer@keycloak.test", "Event", "Sorcerer");

        log.infof("Finished user configuration of realm '%s'.", realmName);
    }

    private void createNewUserIfNotExists(UsersResource usersResource, String username, String email, String firstName, String lastName) {
        List<UserRepresentation> searchResultByUsername = usersResource.search(username);
        List<UserRepresentation> searchResultByEmail = usersResource.searchByEmail(email, true);

        if (searchResultByUsername.isEmpty() && searchResultByEmail.isEmpty()) {
            log.infof("User '%s' does not exist, creating user.", username);
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setUsername(username);
            userRepresentation.setEmail(email.toLowerCase(Locale.ROOT));
            userRepresentation.setFirstName(firstName);
            userRepresentation.setLastName(lastName);
            userRepresentation.setEnabled(true);
            userRepresentation.setEmailVerified(true);
            userRepresentation.setGroups(List.of());
            userRepresentation.setRequiredActions(List.of());

            usersResource.create(userRepresentation);

            UserRepresentation createdUser = usersResource.search(username).getFirst();
            UserResource userResource = usersResource.get(createdUser.getId());
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setValue("test");
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setTemporary(false);

            userResource.resetPassword(credentialRepresentation);
        }

        log.infof("User '%s' already exists, skipping creation.", username);
    }
}
