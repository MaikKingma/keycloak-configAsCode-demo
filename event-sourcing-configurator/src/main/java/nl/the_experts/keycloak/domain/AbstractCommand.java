package nl.the_experts.keycloak.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@EqualsAndHashCode
@ToString
public abstract class AbstractCommand<S, T extends AbstractEvent<S>> {

    @TargetAggregateIdentifier
    private final S id;

    protected AbstractCommand(S id) {
        this.id = id;
    }

    public abstract T convertToEvent();
}