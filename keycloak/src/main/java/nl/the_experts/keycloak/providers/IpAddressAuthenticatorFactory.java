package nl.the_experts.keycloak.providers;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.Collections;
import java.util.List;

public class IpAddressAuthenticatorFactory implements AuthenticatorFactory {
    public static final String PROVIDER_ID = "ip-address-authenticator";

    private static final IpAddressAuthenticator SINGLETON = new IpAddressAuthenticator();

    static final String ALLOWED_IP_ADDRESS_CONFIG_NAME = "allowed_ip_address";

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayType() {
        return "IP Address Authenticator";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return List.of(AuthenticationExecutionModel.Requirement.REQUIRED)
                .toArray(new AuthenticationExecutionModel.Requirement[0]);
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return null;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        ProviderConfigProperty name = new ProviderConfigProperty();

        name.setType(ProviderConfigProperty.STRING_TYPE);
        name.setName(ALLOWED_IP_ADDRESS_CONFIG_NAME);
        name.setLabel("Allowed IP Address which does not require an OTP");
        name.setHelpText("Only accepts one IP address");

        return Collections.singletonList(name);
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return SINGLETON;
    }
}
