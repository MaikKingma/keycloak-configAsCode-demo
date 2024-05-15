package nl.the_experts.keycloak.domain.realm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.io.Serializable;

@Slf4j
@Getter
@Aggregate
@NoArgsConstructor
public class Realm implements Serializable {

    @AggregateIdentifier
    private String name;

    private String displayName;

    @CommandHandler
    public Realm(CreateRealmCommand command) {
        AggregateLifecycle.apply(command.convertToEvent());
    }

    @EventSourcingHandler
    private void on(RealmCreatedEvent event) {
        this.name = event.getRealmName();
        this.displayName = event.getDisplayName();
    }
}
