package nl.the_experts.keycloak.domain.realm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.the_experts.keycloak.domain.client.Client;
import nl.the_experts.keycloak.domain.client.ClientCommand;
import nl.the_experts.keycloak.domain.client.ClientEvent;
import nl.the_experts.keycloak.domain.client.ClientId;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.modelling.command.ForwardMatchingInstances;
import org.axonframework.spring.stereotype.Aggregate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Aggregate
@NoArgsConstructor
public class Realm implements Serializable {

    @AggregateIdentifier
    private String name;

    private String displayName;

    @AggregateMember(eventForwardingMode = ForwardMatchingInstances.class)
    private final List<Client> clients = new ArrayList<>();

    @CommandHandler
    public Realm(RealmCommand.CreateRealmCommand command) {
        AggregateLifecycle.apply(command.convertToEvent());
    }

    @CommandHandler
    public void handleAddClientCommand(ClientCommand.AddClientCommand command) {
        AggregateLifecycle.apply(command.convertToEvent());
    }

    @EventSourcingHandler
    private void onRealmCreatedEvent(RealmEvent.RealmCreatedEvent event) {
        name = event.getRealmName();
        displayName = event.getDisplayName();
    }

    @EventSourcingHandler
    public void onClientAddedEvent(ClientEvent.ClientAddedEvent event) {
        clients.add(new Client(new ClientId(event.getRealmName(), event.getClientId())));
    }
}
