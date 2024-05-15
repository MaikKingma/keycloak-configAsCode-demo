package nl.the_experts.keycloak.data.realm;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.the_experts.keycloak.data.client.ClientProjection;

import java.util.Objects;
import java.util.Set;

@Slf4j
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "realm_projection")
public class RealmProjection {

    @Id
    private String name;

    private String displayName;

    @OneToMany(mappedBy = "realmProjection", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClientProjection> clients;

    @Override
    public final int hashCode() {
        return Objects.hashCode(name);
    }
}
