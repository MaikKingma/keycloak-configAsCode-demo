package nl.the_experts.keycloak.configuration.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.representations.idm.RealmRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RealmConfigurationTest {

    @Mock
    private RealmResource realmResource;

    @Mock
    private RealmsResource realmsResource;

    private RealmConfiguration realmConfiguration;

    @BeforeEach
    void beforeEach() {
        realmConfiguration = new RealmConfiguration(realmsResource);
    }

    @Test
    void configure_givenRealmNotPresent_shouldCreateNewRealm() {
        // given
        when(realmsResource.findAll()).thenReturn(List.of());
        when(realmsResource.realm("example")).thenReturn(realmResource);
        // when
        realmConfiguration.configure(ExampleConfiguration.REALM_NAME, ExampleConfiguration.REALM_DISPLAY_NAME);
        //then
        verify(realmsResource).create(assertArg(result -> assertThat(result)
                .returns(false, RealmRepresentation::isEnabled)
                .returns("example", RealmRepresentation::getRealm)));
    }

    @Test
    void configure_givenRealmPresent_shouldUpdateRealm() {
        // given
        when(realmsResource.findAll()).thenReturn(List.of(createRealmRepresentation("example", "example")));
        when(realmsResource.realm("example")).thenReturn(realmResource);
        // when
        realmConfiguration.configure(ExampleConfiguration.REALM_NAME, ExampleConfiguration.REALM_DISPLAY_NAME);
        //then
        verify(realmResource).update(assertArg(result -> assertThat(result)
                .returns(true, RealmRepresentation::isBruteForceProtected)
                .returns(true, RealmRepresentation::isEnabled)));
        verify(realmsResource, times(0)).create(any());
    }

    private RealmRepresentation createRealmRepresentation(String id, String realm) {
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setId(id);
        realmRepresentation.setRealm(realm);
        realmRepresentation.setEnabled(true);

        return realmRepresentation;
    }
}
