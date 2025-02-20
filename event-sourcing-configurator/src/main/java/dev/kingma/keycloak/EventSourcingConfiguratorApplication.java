package dev.kingma.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
		"org.axonframework.eventsourcing.eventstore.jpa",
		"org.axonframework.eventhandling.tokenstore.jpa",
		"dev.kingma.keycloak.data"
})
public class EventSourcingConfiguratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventSourcingConfiguratorApplication.class, args);
	}

}
