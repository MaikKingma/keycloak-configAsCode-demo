package nl.the_experts.keycloak.command.replay;


import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.SimpleEventHandlerInvoker;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.inmemory.InMemoryTokenStore;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class EventStoreCommands {

    private final EventStore eventStore;
    private final ApplicationContext applicationContext;
    private final TransactionManager transactionManager;

    public EventStoreCommands(EventStore eventStore,
                              ApplicationContext applicationContext,
                              TransactionManager transactionManager
    ) {
        this.eventStore = eventStore;
        this.applicationContext = applicationContext;
        this.transactionManager = transactionManager;
    }

    @PostMapping("/replay")
    public String startReplay(@RequestBody ReplayDetailsPayload replayDetailsPayload) {
        var events = eventStore.readEvents(replayDetailsPayload.aggregateId()).asStream().collect(Collectors.toList());
        log.info("Replaying events: {}", events);

        InMemoryEventStorageEngine engine = new InMemoryEventStorageEngine();
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(ProcessingGroup.class);

        var eventHandlers = beansWithAnnotation.values().stream()
                .filter(bean -> bean.getClass().getAnnotation(ProcessingGroup.class).value().equals("trackingEventProcessor"))
                .collect(Collectors.toList());

        SimpleEventHandlerInvoker invoker = SimpleEventHandlerInvoker.builder()
                .eventHandlers(eventHandlers)
                .build();

        TokenStore inMemoryTokenStore = new InMemoryTokenStore();
        engine.appendEvents(events);

        String name = "trackingEventProcessor-replay-" + replayDetailsPayload.aggregateId();
        EmbeddedEventStore eventStore = EmbeddedEventStore.builder()
                .storageEngine(engine)
                .build();

        TrackingEventProcessor processor = TrackingEventProcessor.builder()
                .name(name)
                .messageSource(eventStore)
                .tokenStore(inMemoryTokenStore)
                .eventHandlerInvoker(invoker)
                .transactionManager(transactionManager)
                .build();

        processor.registerHandlerInterceptor((unitOfWork, interceptorChain) -> interceptorChain.proceed());
        processor.start();

        boolean completed = false;
        int iterations = 0;

        while (!completed && iterations < 60) {
            log.info("Waiting until replay for visit is completed, iteration {}/60...", iterations);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Thread was interrupted", e);
            }

            long currentTokenPosition = inMemoryTokenStore.fetchToken(name, 0).position().orElse(-1L);
            long headTokenPosition = engine.createHeadToken().position().orElse(-1L);
            completed = currentTokenPosition == headTokenPosition;
            iterations++;
        }

        log.info("Stopping processor...");
        processor.shutDown();

        return "OK, replay done";
    }

    public record ReplayDetailsPayload(String aggregateId) {
    }
}