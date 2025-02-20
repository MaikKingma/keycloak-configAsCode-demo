package dev.kingma.keycloak.domain.realm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import dev.kingma.keycloak.domain.AbstractEvent;

public sealed interface RealmEvent permits RealmEvent.RealmCreatedEvent {

    @Value
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    class RealmCreatedEvent extends AbstractEvent<String> implements RealmEvent {

        String displayName;

        @JsonCreator
        public RealmCreatedEvent(
                @JsonProperty("name") String name,
                @JsonProperty("displayName") String displayName
        ) {
            super(name);
            this.displayName = displayName;
        }

        public String getRealmName() {
            return getId();
        }
    }
}
