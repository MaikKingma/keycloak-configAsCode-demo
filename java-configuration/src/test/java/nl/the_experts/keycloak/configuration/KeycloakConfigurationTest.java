package nl.the_experts.keycloak.configuration;

import nl.the_experts.keycloak.configuration.eventsorcerer.EventSorcererConfiguration;
import nl.the_experts.keycloak.configuration.example.ExampleConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
        var exampleConfigurationMockedConstruction = mockConstruction(ExampleConfiguration.class);
        var eventSorcererMock = mockConstruction(EventSorcererConfiguration.class);
        // when
        keycloakConfiguration.configure();
        // then
        var exampleConfiguration = exampleConfigurationMockedConstruction.constructed().getFirst();
        var eventSorcererConfiguration = eventSorcererMock.constructed().getFirst();
        verify(exampleConfiguration, times(1)).configure();
        verify(eventSorcererConfiguration, times(1)).configure();
        // finally
        exampleConfigurationMockedConstruction.close();
        eventSorcererMock.close();
    }
}
