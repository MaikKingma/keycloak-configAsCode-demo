package dev.kingma.keycloak.configuration.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExampleConfigurationTest {

    @Mock
    Keycloak keycloak;

    @InjectMocks
    private ExampleConfiguration exampleConfiguration;

    @Test
    void configure_shouldCallAllRequiredMethods() {
        // given
        MockedConstruction<RealmConfiguration> realmConfigConstructorMock = mockConstruction(RealmConfiguration.class);
        // when
        exampleConfiguration.configure();
        // then
        RealmConfiguration mockRealm = realmConfigConstructorMock.constructed().getFirst();
        verify(mockRealm, times(1)).configure(ExampleConfiguration.REALM_NAME, ExampleConfiguration.REALM_DISPLAY_NAME);
        realmConfigConstructorMock.close();
    }
}
