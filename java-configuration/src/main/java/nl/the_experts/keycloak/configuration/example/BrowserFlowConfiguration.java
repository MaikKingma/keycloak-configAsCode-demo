package nl.the_experts.keycloak.configuration.example;

import lombok.Setter;
import lombok.extern.jbosslog.JBossLog;
import nl.the_experts.keycloak.configuration.KeycloakConfigurationProperties;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.AuthenticationExecutionInfoRepresentation;
import org.keycloak.representations.idm.AuthenticationFlowRepresentation;
import org.keycloak.representations.idm.AuthenticatorConfigRepresentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@JBossLog
public class BrowserFlowConfiguration {

    private RealmResource realmResource;

    private static final String ALIAS = "IP address OTP browser flow";
    private static final String USERNAME_PASSWORD_FORM_PROVIDER_ID = "auth-username-password-form";
    private static final String IP_ADDRESS_AUTHENTICATOR_PROVIDER_ID = "ip-address-authenticator";
    private static final String CONDITIONAL_OTP_FORM_PROVIDER_ID = "auth-conditional-otp-form";

    private static final List<String> REQUIRED_EXECUTIONS = List.of(
            USERNAME_PASSWORD_FORM_PROVIDER_ID,
            IP_ADDRESS_AUTHENTICATOR_PROVIDER_ID,
            CONDITIONAL_OTP_FORM_PROVIDER_ID
    );

    public String createBrowserFlow() {
        AuthenticationFlowRepresentation authenticationFlow = new AuthenticationFlowRepresentation();
        authenticationFlow.setAlias(ALIAS);
        authenticationFlow.setDescription("Custom browser based authentication");
        authenticationFlow.setProviderId("basic-flow");
        authenticationFlow.setBuiltIn(false);
        authenticationFlow.setTopLevel(true);

        realmResource.flows().createFlow(authenticationFlow);

        addExecution(ALIAS, "auth-cookie");
        addExecution(ALIAS, "identity-provider-redirector");

        String registrationPageFormExecutionFlowAlias = addExecutionFlow(ALIAS, "registration-page-form");

        addExecution(registrationPageFormExecutionFlowAlias, USERNAME_PASSWORD_FORM_PROVIDER_ID);
        addExecution(registrationPageFormExecutionFlowAlias, IP_ADDRESS_AUTHENTICATOR_PROVIDER_ID);
        addExecution(registrationPageFormExecutionFlowAlias, CONDITIONAL_OTP_FORM_PROVIDER_ID);

        List<AuthenticationExecutionInfoRepresentation> registrationPageFlowExecutions = realmResource.flows().getExecutions(registrationPageFormExecutionFlowAlias);

        String ipAddressProviderExecutionId = registrationPageFlowExecutions.stream()
                .filter(execution -> execution.getProviderId().equals(IP_ADDRESS_AUTHENTICATOR_PROVIDER_ID))
                .findFirst().orElseThrow().getId();

        addIpAddressAuthenticatorExecutionConfig(ipAddressProviderExecutionId);

        String otpFormProviderExecutionId = registrationPageFlowExecutions.stream()
                .filter(execution -> execution.getProviderId().equals(CONDITIONAL_OTP_FORM_PROVIDER_ID))
                .findFirst().orElseThrow().getId();

        addOTPFormExecutionConfig(otpFormProviderExecutionId);

        List<AuthenticationExecutionInfoRepresentation> executions = realmResource.flows().getExecutions(ALIAS);

        for (AuthenticationExecutionInfoRepresentation execution : executions) {
            if (execution.getProviderId() != null && REQUIRED_EXECUTIONS.contains(execution.getProviderId())) {
                execution.setRequirement("REQUIRED");
            } else {
                execution.setRequirement("ALTERNATIVE");
            }

            realmResource.flows().updateExecutions(ALIAS, execution);
        }

        return ALIAS;
    }

    private String addExecutionFlow(String executionAlias, String provider) {
        String providerExecutionFlowAlias = provider + " - " + executionAlias;

        Map<String, String> executionData = new HashMap<>(3);
        executionData.put("provider", provider);
        executionData.put("alias", providerExecutionFlowAlias);
        executionData.put("type", "basic-flow");

        realmResource.flows().addExecutionFlow(executionAlias, executionData);

        return providerExecutionFlowAlias;
    }

    private void addExecution(String executionAlias, String provider) {
        Map<String, String> executionData = new HashMap<>(1);
        executionData.put("provider", provider);

        realmResource.flows().addExecution(executionAlias, executionData);
    }

    private void addIpAddressAuthenticatorExecutionConfig(String id) {
        KeycloakConfigurationProperties configuration = KeycloakConfigurationProperties.fromEnv();
        String allowedIpAddress = configuration.get("KEYCLOAK_ALLOWED_IP_ADDRESS");

        Map<String, String> config = new HashMap<>();
        config.put("allowed_ip_address", allowedIpAddress);

        updateExecutionConfig(id, "IP address authenticator", config);
    }

    private void addOTPFormExecutionConfig(String id) {
        Map<String, String> config = new HashMap<>(2);
        config.put("otpControlAttribute", "ip_address_otp_conditional");
        config.put("defaultOtpOutcome", "force");

        updateExecutionConfig(id, "Conditional OTP Form", config);
    }

    private void updateExecutionConfig(String id, String alias, Map<String, String> config) {
        AuthenticatorConfigRepresentation authenticatorConfigRepresentation = new AuthenticatorConfigRepresentation();
        authenticatorConfigRepresentation.setAlias(alias);
        authenticatorConfigRepresentation.setConfig(config);

        realmResource.flows().newExecutionConfig(id, authenticatorConfigRepresentation);
    }
}
