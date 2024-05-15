package nl.the_experts.keycloak.domain.client;

import lombok.*;

import java.io.Serializable;

@Value
public class ClientId implements Serializable {
    String realmName;
    String clientId;
}
