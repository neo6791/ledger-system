package neo.ledger.query.controller.dto;

import java.math.BigDecimal;

public record LedgerAccountBalanceDTO(String accountId, BigDecimal balance) {
}
