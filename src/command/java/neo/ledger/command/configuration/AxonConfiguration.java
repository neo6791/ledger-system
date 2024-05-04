package neo.ledger.command.configuration;

import neo.ledger.command.LedgerAccountAggregate;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfiguration {

    @Bean
    public EventSourcingRepository<LedgerAccountAggregate> eventSourcingRepository(EventStore eventStore) {
        return EventSourcingRepository.builder(LedgerAccountAggregate.class)
                .eventStore(eventStore).build();
    }

}