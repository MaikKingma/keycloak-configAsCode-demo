package dev.kingma.keycloak.domain.client;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import dev.kingma.keycloak.domain.AbstractCommand;

public sealed interface ClientCommand permits ClientCommand.AddClientCommand{

    @Value
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    class AddClientCommand extends AbstractCommand<String, ClientEvent.ClientAddedEvent> implements ClientCommand{

        String clientId;

        public AddClientCommand(String realmName, String clientId) {
            super(realmName);
            this.clientId = clientId;
        }

        @Override
        public ClientEvent.ClientAddedEvent convertToEvent() {
            return new ClientEvent.ClientAddedEvent(getRealmName(), clientId);
        }

        private String getRealmName() {
            return getId();
        }
    }
}
