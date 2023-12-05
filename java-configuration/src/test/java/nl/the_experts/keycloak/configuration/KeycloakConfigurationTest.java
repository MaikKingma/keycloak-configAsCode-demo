package nl.the_experts.keycloak.configuration;

import nl.the_experts.keycloak.configuration.example.ExampleConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KeycloakConfigurationTest {

    @InjectMocks
    private KeycloakConfiguration keycloakConfiguration;

    @Test
    void configure_shouldCallsConfigureForExampleRealms() {
        // given
        MockedConstruction<ExampleConfiguration> exampleConfigurationMockedConstruction =
                mockConstruction(ExampleConfiguration.class);
        // when
        keycloakConfiguration.configure();
        // then
        ExampleConfiguration exampleConfiguration = exampleConfigurationMockedConstruction.constructed().get(0);
        verify(exampleConfiguration, times(1)).configure();
        // finally
        exampleConfigurationMockedConstruction.close();
    }
}
