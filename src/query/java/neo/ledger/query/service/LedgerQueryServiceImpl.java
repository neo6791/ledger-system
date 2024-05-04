package neo.ledger.query.service;

import neo.ledger.common.event.LedgerEntryEvent;
import neo.ledger.common.repository.LedgerAccountBalanceEntity;
import neo.ledger.common.repository.LedgerAccountBalanceRepository;
import neo.ledger.query.controller.dto.LedgerAccountBalanceDTO;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LedgerQueryServiceImpl implements LedgerQueryService {

    private final EventStore eventStore;
    private final LedgerAccountBalanceRepository ledgerAccountBalanceRepository;

    public LedgerQueryServiceImpl(EventStore eventStore, LedgerAccountBalanceRepository ledgerAccountBalanceRepository) {
        this.eventStore = eventStore;
        this.ledgerAccountBalanceRepository = ledgerAccountBalanceRepository;
    }

    @Override
    public List<Object> listEventsForAccount(String accountNumber) {
        return eventStore.readEvents(accountNumber).asStream().map(Message::getPayload)
                .collect(Collectors.toList());
    }

    @Override
    public LedgerAccountBalanceDTO readLedgerAccountBalance(String accountId) {
        LedgerAccountBalanceEntity entity = ledgerAccountBalanceRepository
                .findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        return new LedgerAccountBalanceDTO(entity.getId(), entity.getBalance());
    }

    @Override
    public BigDecimal readLedgerAccountBalanceOn(String accountId, ZonedDateTime zonedDateTime) {
        return eventStore
                .readEvents(accountId)
                .asStream()
                .map(s -> {
                    if (s.getPayload() instanceof LedgerEntryEvent entry) {
                        return entry;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .filter(le -> le.getEntryDate().isBefore(zonedDateTime))
                .map(f -> {
                    if ("DR".equalsIgnoreCase(f.getType())) {
                        return f.getAmount();
                    } else {
                        return f.getAmount().negate();
                    }
                })
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}