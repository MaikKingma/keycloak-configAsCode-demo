package nl.the_experts.keycloak.domain.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.modelling.command.EntityId;

@Builder
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @EntityId
    private ClientId clientId;
}
