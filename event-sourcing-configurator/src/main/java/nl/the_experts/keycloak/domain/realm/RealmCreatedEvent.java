package nl.the_experts.keycloak.domain.realm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import nl.the_experts.keycloak.domain.AbstractEvent;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RealmCreatedEvent extends AbstractEvent<String> {

    String displayName;

    @JsonCreator
    public RealmCreatedEvent(@JsonProperty("name") String name,
                             @JsonProperty("displayName") String displayName) {
        super(name);
        this.displayName = displayName;
    }

    public String getRealmName() {
        return getId();
    }
}
