package neo.ledger.query.service;

import neo.ledger.query.controller.dto.LedgerAccountBalanceDTO;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public interface LedgerQueryService {
    List<Object> listEventsForAccount(String accountNumber);

    LedgerAccountBalanceDTO readLedgerAccountBalance(String accountId);

    BigDecimal readLedgerAccountBalanceOn(String accountId, ZonedDateTime zonedDateTime);
}
