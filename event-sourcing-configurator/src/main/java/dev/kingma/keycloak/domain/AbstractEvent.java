package dev.kingma.keycloak.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public abstract class AbstractEvent<T> {

    private final T id;

    @Setter
    private boolean isManualReplay;

    protected AbstractEvent(T id) {
        super();
        this.id = id;
        isManualReplay = false;
    }
}
