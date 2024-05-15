package nl.the_experts.keycloak.data.client;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.the_experts.keycloak.data.realm.RealmProjection;


@Slf4j
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "client_projection")
public class ClientProjection{

    @EmbeddedId
    private ClientProjectionId clientProjectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "realmName", referencedColumnName = "name", insertable = false, updatable = false)
    })
    private RealmProjection realmProjection;
}
