package neo.ledger.common.repository;

import org.springframework.data.repository.CrudRepository;

public interface LedgerAccountBalanceRepository extends CrudRepository<LedgerAccountBalanceEntity, String> {
}
