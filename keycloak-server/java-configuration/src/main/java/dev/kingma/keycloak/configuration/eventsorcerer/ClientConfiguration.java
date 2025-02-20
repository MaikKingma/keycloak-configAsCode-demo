package dev.kingma.keycloak.configuration.eventsorcerer;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.representations.idm.ClientRepresentation;

import java.util.HashMap;
import java.util.List;

@JBossLog
@AllArgsConstructor
public class ClientConfiguration {

    private static final String EVENT_SORCERER_CLIENT_ID = "event-sorcerer";
    private static final String EVENT_SORCERER_DASHBOARD_HOST = "http://localhost:3000";

    private final RealmsResource realmsResource;

    public void configure(String realmName) {
        log.info("Configuring client");
        RealmResource realm = realmsResource.realm(realmName);
        ClientsResource clientsResource = realm.clients();
        List<ClientRepresentation> existingClients = clientsResource.findAll();

        configureEventSorcererClient(existingClients, clientsResource);
    }

    private void configureEventSorcererClient(List<ClientRepresentation> clients, ClientsResource clientsResource) {
        if (clients.isEmpty() || clients.stream().noneMatch(client -> client.getClientId().equals(EVENT_SORCERER_CLIENT_ID))) {
            log.infof("Client does not yet exist, creating for client: %s", EVENT_SORCERER_CLIENT_ID);
            createNewClient(clientsResource, EVENT_SORCERER_CLIENT_ID);
        }
        updateClientSecret(clientsResource);
    }

    private void updateClientSecret(ClientsResource clientsResource) {
        ClientRepresentation clientRepresentation = clientsResource.findByClientId(EVENT_SORCERER_CLIENT_ID).getFirst();
        clientRepresentation.setSecret("some-awesome-sorcerers-magic-key");
        clientsResource.get(clientRepresentation.getId()).update(clientRepresentation);
        log.infof("Updated client secret for client %s", EVENT_SORCERER_CLIENT_ID);
    }

    private void createNewClient(ClientsResource clientsResource, String clientId) {
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(clientId);

        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("auth2.device.authorization.grant.enabled", Boolean.FALSE.toString());
        attributes.put("oidc.ciba.grant.enabled", Boolean.FALSE.toString());
        attributes.put("saml_idp_initiated_sso_url_name", "");
        attributes.put("post.logout.redirect.uris", EVENT_SORCERER_DASHBOARD_HOST + "/*");
        clientRepresentation.setAttributes(attributes);

        clientRepresentation.setBaseUrl("");
        clientRepresentation.setRootUrl("");
        clientRepresentation.setRedirectUris(List.of(EVENT_SORCERER_DASHBOARD_HOST + "/api/auth/callback/keycloak"));
        clientRepresentation.setProtocol("openid-connect");

        clientRepresentation.setAuthorizationServicesEnabled(false);
        clientRepresentation.setAlwaysDisplayInConsole(false);

        clientRepresentation.setPublicClient(false);
        clientRepresentation.setFrontchannelLogout(true);
        clientRepresentation.setServiceAccountsEnabled(false);
        clientRepresentation.setStandardFlowEnabled(true);
        clientRepresentation.setDirectAccessGrantsEnabled(false);
        clientRepresentation.setImplicitFlowEnabled(false);

        clientsResource.create(clientRepresentation);

        log.infof("Created client %s", clientId);
    }

}
