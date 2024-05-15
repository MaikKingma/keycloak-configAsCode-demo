package nl.the_experts.keycloak.domain.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import nl.the_experts.keycloak.domain.AbstractEvent;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ClientAddedEvent extends AbstractEvent<String> {

    String clientId;

    @JsonCreator
    public ClientAddedEvent(@JsonProperty("realmName") String realmName, String clientId) {
        super(realmName);
        this.clientId = clientId;
    }

    public String getRealmName() {
        return getId();
    }
}
