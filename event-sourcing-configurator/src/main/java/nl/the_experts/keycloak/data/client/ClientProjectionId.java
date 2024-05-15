package nl.the_experts.keycloak.data.client;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ClientProjectionId implements Serializable {
    String realmName;
    String clientId;

    @Override
    public boolean equals(Object object) {
        if (object instanceof ClientProjectionId other) {
            return Objects.equals(clientId, other.clientId) &&
                    realmName.equals(other.realmName);
        } else return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
