package nl.the_experts.keycloak.domain.client;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import nl.the_experts.keycloak.domain.AbstractCommand;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AddClientCommand extends AbstractCommand<String, ClientAddedEvent> {

    String clientId;

    public AddClientCommand(String realmName, String clientId) {
        super(realmName);
        this.clientId = clientId;
    }

    @Override
    public ClientAddedEvent convertToEvent() {
        return new ClientAddedEvent(getRealmName(), clientId);
    }

    private String getRealmName() {
        return getId();
    }
}