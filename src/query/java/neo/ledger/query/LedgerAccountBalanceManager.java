package neo.ledger.query;

import neo.ledger.common.event.LedgerAccountBalanceEvent;
import neo.ledger.common.repository.LedgerAccountBalanceEntity;
import neo.ledger.common.repository.LedgerAccountBalanceRepository;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.springframework.stereotype.Component;

@Component
public class LedgerAccountBalanceManager {

    private final LedgerAccountBalanceRepository ledgerAccountBalanceRepository;

//    private final EventSourcingRepository<LedgerAccountAggregate> accountAggregateRepository;

    public LedgerAccountBalanceManager(LedgerAccountBalanceRepository ledgerAccountBalanceRepository) {
        this.ledgerAccountBalanceRepository = ledgerAccountBalanceRepository;
    }

    @EventSourcingHandler
    void on(LedgerAccountBalanceEvent event) {
        persistAccount(buildQueryAccount(event));
    }

    private LedgerAccountBalanceEntity buildQueryAccount(LedgerAccountBalanceEvent accountBalanceEvent) {
        LedgerAccountBalanceEntity ledgerAccountBalanceEntity = findExistingOrCreateQueryAccount(accountBalanceEvent.getAccountId());

        ledgerAccountBalanceEntity.setId(accountBalanceEvent.getAccountId());
        ledgerAccountBalanceEntity.setBalance(accountBalanceEvent.getBalance());

        return ledgerAccountBalanceEntity;
    }

//    private LedgerAccountAggregate getAccountFromEvent(LedgerEntryEvent event) {
//        return accountAggregateRepository.load(event.accountId()).getWrappedAggregate().getAggregateRoot();
//    }

    private LedgerAccountBalanceEntity findExistingOrCreateQueryAccount(String id) {
        return ledgerAccountBalanceRepository.findById(id).isPresent() ? ledgerAccountBalanceRepository.findById(id).get() : new LedgerAccountBalanceEntity();
    }


    private void persistAccount(LedgerAccountBalanceEntity accountQueryEntity) {
        ledgerAccountBalanceRepository.save(accountQueryEntity);
    }
}
