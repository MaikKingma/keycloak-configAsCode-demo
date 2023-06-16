package nl.the_experts.keycloak.configuration.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.representations.idm.RealmRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RealmConfigurationTest {

    @Captor
    ArgumentCaptor<RealmRepresentation> realmRepresentationCaptor;

    private RealmConfiguration realmConfiguration;

    @BeforeEach
    void setUp() {

    }

    @Test
    void configure_givenRealmNotPresent_shouldCreateNewRealm() {
        // given
        RealmsResource realmsResource = Mockito.mock(RealmsResource.class);
        realmConfiguration = new RealmConfiguration(realmsResource);
        RealmResource realmResource = Mockito.mock(RealmResource.class);
        when(realmsResource.findAll()).thenReturn(List.of());
        when(realmsResource.realm("example")).thenReturn(realmResource);
        // when
        realmConfiguration.configure(ExampleConfiguration.REALM_NAME, ExampleConfiguration.REALM_DISPLAY_NAME);
        //then
        assertThat(realmsResource.findAll()).isEmpty();
        verify(realmsResource).create(realmRepresentationCaptor.capture());
        RealmRepresentation value = realmRepresentationCaptor.getValue();
        assertThat(value)
                .returns(false, RealmRepresentation::isEnabled)
                .returns("example", RealmRepresentation::getRealm);
    }

//    @Test
//    void configure_givenRealmPresent_shouldUpdateRealm() {
//        // given
//        when(realmsResource.findAll()).thenReturn(List.of(createRealmRepresentation()));
//        when(realmsResource.realm("example")).thenReturn(realmResource);
//        // when
//        realmConfiguration.configure(ExampleConfiguration.REALM_NAME, ExampleConfiguration.REALM_DISPLAY_NAME);
//        //then
//        verify(realmResource).update(realmRepresentationCaptor.capture());
//        RealmRepresentation result = realmRepresentationCaptor.getValue();
//        assertThat(result)
//                .returns(true, RealmRepresentation::isBruteForceProtected)
//                .returns(true, RealmRepresentation::isEnabled);
//        verify(realmsResource, times(0)).create(any());
//    }

    private RealmRepresentation createRealmRepresentation() {
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setId("example");
        realmRepresentation.setRealm("example");
        realmRepresentation.setEnabled(true);
        return realmRepresentation;
    }
}
