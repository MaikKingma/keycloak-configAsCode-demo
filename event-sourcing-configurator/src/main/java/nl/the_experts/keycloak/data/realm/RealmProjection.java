package nl.the_experts.keycloak.data.realm;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

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

    @Override
    public final int hashCode() {
        return Objects.hashCode(name);
    }
}
