package dev.kingma.keycloak.acl.keycloak.client;

import dev.kingma.keycloak.acl.keycloak.KeycloakClient;
import dev.kingma.keycloak.domain_interaction.client.ClientService;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ClientServiceImpl implements ClientService {

    private final KeycloakClient keycloakClient;

    public ClientServiceImpl(KeycloakClient keycloakClient) {
        this.keycloakClient = keycloakClient;
    }

    @Override
    public void createRealmClient(String realmName, String clientId) {
        RealmResource realmResource = keycloakClient.get().realm(realmName);
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(clientId);
        clientRepresentation.setName("testName");
        clientRepresentation.setDescription("test description");

        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("auth2.device.authorization.grant.enabled", Boolean.FALSE.toString());
        attributes.put("oidc.ciba.grant.enabled", Boolean.FALSE.toString());
        attributes.put("saml_idp_initiated_sso_url_name", "");
        clientRepresentation.setAttributes(attributes);

        clientRepresentation.setBaseUrl("");
        clientRepresentation.setRootUrl("");
        clientRepresentation.setProtocol("openid-connect");

        clientRepresentation.setAuthorizationServicesEnabled(false);
        clientRepresentation.setAlwaysDisplayInConsole(false);
        clientRepresentation.setDirectAccessGrantsEnabled(true);
        clientRepresentation.setFrontchannelLogout(true);
        clientRepresentation.setImplicitFlowEnabled(false);
        clientRepresentation.setPublicClient(true);
        clientRepresentation.setServiceAccountsEnabled(false);
        clientRepresentation.setStandardFlowEnabled(true);

        realmResource.clients().create(clientRepresentation);

        realmResource.clients().get(clientId).roles().create(new RoleRepresentation("admin", "admin", false));
    }
}
