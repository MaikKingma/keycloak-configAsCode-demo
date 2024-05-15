package nl.the_experts.keycloak.domain.realm;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import nl.the_experts.keycloak.domain.AbstractCommand;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CreateRealmCommand extends AbstractCommand<String, RealmCreatedEvent> {
    String displayName;

    public CreateRealmCommand(String name, String displayName) {
        super(name);
        this.displayName = displayName;
    }

    public String getRealmName() {
        return getId();
    }

    @Override
    public RealmCreatedEvent convertToEvent() {
        return new RealmCreatedEvent(getRealmName(), displayName);
    }
}
