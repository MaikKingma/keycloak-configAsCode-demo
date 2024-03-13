package nl.the_experts.keycloak.providers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.Collections;

public class IpAddressAuthenticator implements Authenticator {

    private static final Log LOGGER = LogFactory.getLog(IpAddressAuthenticator.class);
    private static final String IP_ADDRESS_OTP_CONDITIONAL_USER_ATTRIBUTE = "ip_address_otp_conditional";

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        String allowedIPAddress =  context.getAuthenticatorConfig().getConfig()
                .get(IpAddressAuthenticatorFactory.ALLOWED_IP_ADDRESS_CONFIG_NAME);

        String remoteIPAddress = context.getConnection().getRemoteAddr();

        UserModel user = context.getUser();

        if (!allowedIPAddress.equals(remoteIPAddress)) {
            LOGGER.info(String.format("IP address (%s) of user (%s) does not match allowed IP address", remoteIPAddress, user.getId()));
            user.setAttribute(IP_ADDRESS_OTP_CONDITIONAL_USER_ATTRIBUTE, Collections.singletonList("force"));
        } else {
            user.setAttribute(IP_ADDRESS_OTP_CONDITIONAL_USER_ATTRIBUTE, Collections.singletonList("skip"));
        }

        context.success();
    }

    @Override
    public void action(AuthenticationFlowContext context) {
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

    }

    @Override
    public void close() {
    }
}
