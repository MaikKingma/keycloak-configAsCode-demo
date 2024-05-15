package nl.the_experts.keycloak.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public abstract class AbstractEvent<T> {

    private final T id;

    protected AbstractEvent(T id) {
        super();
        this.id = id;
    }

}
