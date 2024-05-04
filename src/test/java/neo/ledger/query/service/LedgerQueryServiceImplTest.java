package neo.ledger.query.service;

import neo.ledger.common.event.LedgerEntryEvent;
import neo.ledger.common.repository.LedgerAccountBalanceEntity;
import neo.ledger.common.repository.LedgerAccountBalanceRepository;
import neo.ledger.query.controller.dto.LedgerAccountBalanceDTO;
import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventhandling.GenericDomainEventMessage;
import org.axonframework.eventsourcing.eventstore.DomainEventStream;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LedgerQueryServiceImplTest {

    @Mock
    EventStore eventStore;
    @Mock
    LedgerAccountBalanceRepository ledgerAccountBalanceRepository;

    LedgerQueryServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new LedgerQueryServiceImpl(eventStore, ledgerAccountBalanceRepository);
    }

    @Test
    void validAccount_readLedgerAccountBalance_returnedOK() {

        LedgerAccountBalanceEntity entity = new LedgerAccountBalanceEntity();
        entity.setId("1");
        entity.setBalance(BigDecimal.TEN);
        when(ledgerAccountBalanceRepository.findById("1")).thenReturn(Optional.of(entity));
        LedgerAccountBalanceDTO dto = underTest.readLedgerAccountBalance("1");
        assertThat(dto.balance(), equalTo(BigDecimal.TEN));

    }

    @Test
    void invalidAccount_throws_invalidAccount() {
        when(ledgerAccountBalanceRepository.findById("1")).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> underTest.readLedgerAccountBalance("1"));
    }


    @Test
    void threeDebits_readLedgerAccountBalanceNow_calculatedOK() {

        DomainEventMessage<LedgerEntryEvent> firstEvent =
                new GenericDomainEventMessage<>("type", "1", 0L,
                        buildLedgerEntryEvent(ZonedDateTime.now().minusDays(3), "DR"));

        DomainEventMessage<LedgerEntryEvent> secondEvent =
                new GenericDomainEventMessage<>("type", "1", 1L,
                        buildLedgerEntryEvent(ZonedDateTime.now().minusDays(2), "DR"));

        DomainEventMessage<LedgerEntryEvent> thirdEvent =
                new GenericDomainEventMessage<>("type", "1", 2L,
                        buildLedgerEntryEvent(ZonedDateTime.now().minusDays(1), "DR"));

        when(eventStore.readEvents("1"))
                .thenReturn(DomainEventStream.of(firstEvent, secondEvent, thirdEvent));

        BigDecimal ret = underTest.readLedgerAccountBalanceOn("1", ZonedDateTime.now());
        assertThat(ret, equalTo(new BigDecimal(30)));

    }

    @Test
    void twoDebitsAndOneCredit_readLedgerAccountBalanceNow_CalculatedOK() {

        DomainEventMessage<LedgerEntryEvent> firstEvent =
                new GenericDomainEventMessage<>("type", "1", 0L,
                        buildLedgerEntryEvent(ZonedDateTime.now().minusDays(3), "DR"));

        DomainEventMessage<LedgerEntryEvent> secondEvent =
                new GenericDomainEventMessage<>("type", "1", 1L,
                        buildLedgerEntryEvent(ZonedDateTime.now().minusDays(2), "DR"));

        DomainEventMessage<LedgerEntryEvent> thirdEvent =
                new GenericDomainEventMessage<>("type", "1", 2L,
                        buildLedgerEntryEvent(ZonedDateTime.now().minusDays(1), "CR"));

        when(eventStore.readEvents("1"))
                .thenReturn(DomainEventStream.of(firstEvent, secondEvent, thirdEvent));

        BigDecimal ret = underTest.readLedgerAccountBalanceOn("1", ZonedDateTime.now());
        assertThat(ret, equalTo(new BigDecimal(10)));

    }

    @Test
    void threeDebits_readLedgerAccountBalanceForDateInThePast_calculatedToBeZero() {

        DomainEventMessage<LedgerEntryEvent> firstEvent =
                new GenericDomainEventMessage<>("type", "1", 0L,
                        buildLedgerEntryEvent(ZonedDateTime.now().minusDays(3), "DR"));

        DomainEventMessage<LedgerEntryEvent> secondEvent =
                new GenericDomainEventMessage<>("type", "1", 1L,
                        buildLedgerEntryEvent(ZonedDateTime.now().minusDays(2), "DR"));

        DomainEventMessage<LedgerEntryEvent> thirdEvent =
                new GenericDomainEventMessage<>("type", "1", 2L,
                        buildLedgerEntryEvent(ZonedDateTime.now().minusDays(1), "DR"));

        when(eventStore.readEvents("1"))
                .thenReturn(DomainEventStream.of(firstEvent, secondEvent, thirdEvent));

        BigDecimal ret = underTest.readLedgerAccountBalanceOn("1", ZonedDateTime.now().minusDays(5));
        assertThat(ret, equalTo(BigDecimal.ZERO));

    }

    private LedgerEntryEvent buildLedgerEntryEvent(ZonedDateTime entryDate, String type) {
        return new LedgerEntryEvent("1", entryDate, "description", new BigDecimal(10), type);
    }

}