package nl.the_experts.keycloak.configuration;


import org.junit.jupiter.api.Test;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.SetSystemProperty;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KeycloakConfigurationApplicationTest {

    @Test
    @SetSystemProperty(key = "KEYCLOAK_SERVER", value = "server_test")
    @SetSystemProperty(key = "KEYCLOAK_USER", value = "user_test")
    @SetSystemProperty(key = "KEYCLOAK_PASSWORD", value = "password_test")
    @SetSystemProperty(key = "KEYCLOAK_REALM", value = "realm_test")
    void main_shouldCreateKeycloakClient() {
        MockedStatic<KeycloakConfigurationProperties> keycloakConfigPropertiesMockedStatic =
                mockStatic(KeycloakConfigurationProperties.class, CALLS_REAL_METHODS);
        MockedStatic<KeycloakClientBuilder> keycloakClientBuilderMockedStatic =
                mockStatic(KeycloakClientBuilder.class, CALLS_REAL_METHODS);
        MockedConstruction<KeycloakConfiguration> keycloakConfigurationMockedConstruction =
                mockConstruction(KeycloakConfiguration.class);

        // when
        KeycloakConfigurationApplication.main(Arrays.array("test"));
        // then
        keycloakConfigPropertiesMockedStatic.verify(KeycloakConfigurationProperties::fromEnv, times(1));
        keycloakClientBuilderMockedStatic.verify(() ->
                KeycloakClientBuilder.create(
                        "server_test",
                        "user_test",
                        "password_test",
                        "realm_test"
                ), times(1));
        KeycloakConfiguration keycloakConfiguration = keycloakConfigurationMockedConstruction.constructed().get(0);
        verify(keycloakConfiguration, times(1)).configure();

        keycloakConfigPropertiesMockedStatic.close();
        keycloakClientBuilderMockedStatic.close();
        keycloakConfigurationMockedConstruction.close();
    }
}
