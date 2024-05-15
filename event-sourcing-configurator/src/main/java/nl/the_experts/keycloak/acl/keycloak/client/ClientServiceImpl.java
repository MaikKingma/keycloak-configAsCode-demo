package nl.the_experts.keycloak.acl.keycloak.client;

import nl.the_experts.keycloak.acl.keycloak.KeycloakClient;
import nl.the_experts.keycloak.domain_interaction.client.ClientService;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
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
        clientRepresentation.setName("");
        clientRepresentation.setDescription("");

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
    }
}