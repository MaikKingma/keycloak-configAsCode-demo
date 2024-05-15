package nl.the_experts.keycloak.command.client;

import nl.the_experts.keycloak.acl.KeycloakClient;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/realms/{realmName}/clients")
public class RealmClientCommands {

    private final KeycloakClient keycloakClient;

    public RealmClientCommands(KeycloakClient keycloakClient) {
        this.keycloakClient = keycloakClient;
    }

    @PostMapping("create")
    public void createClient(@PathVariable String realmName, @RequestBody CreateClientPayload createClientPayload) {
        RealmResource realmResource = keycloakClient.get().realm(realmName);
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(createClientPayload.clientId());
        clientRepresentation.setAlwaysDisplayInConsole(false);
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("auth2.device.authorization.grant.enabled", Boolean.FALSE.toString());
        attributes.put("oidc.ciba.grant.enabled", Boolean.FALSE.toString());
        attributes.put("saml_idp_initiated_sso_url_name", "");
        clientRepresentation.setAttributes(attributes);
        clientRepresentation.setAuthorizationServicesEnabled(false);
        clientRepresentation.setBaseUrl("");
        clientRepresentation.setDescription("");
        clientRepresentation.setDirectAccessGrantsEnabled(true);
        clientRepresentation.setFrontchannelLogout(true);
        clientRepresentation.setImplicitFlowEnabled(false);
        clientRepresentation.setName("");
        clientRepresentation.setProtocol("openid-connect");
        clientRepresentation.setPublicClient(true);
        clientRepresentation.setRootUrl("");
        clientRepresentation.setServiceAccountsEnabled(false);
        clientRepresentation.setStandardFlowEnabled(true);

        realmResource.clients().create(clientRepresentation);
    }

    public record CreateClientPayload(String clientId) {
    }
}
