package dev.kingma.keycloak.domain.realm;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import dev.kingma.keycloak.domain.AbstractCommand;

public sealed interface RealmCommand permits RealmCommand.CreateRealmCommand {
    @Value
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    class CreateRealmCommand extends AbstractCommand<String, RealmEvent.RealmCreatedEvent> implements RealmCommand {
        String displayName;

        public CreateRealmCommand(String name, String displayName) {
            super(name);
            this.displayName = displayName;
        }

        public String getRealmName() {
            return getId();
        }

        @Override
        public RealmEvent.RealmCreatedEvent convertToEvent() {
            return new RealmEvent.RealmCreatedEvent(getRealmName(), displayName);
        }
    }
}
