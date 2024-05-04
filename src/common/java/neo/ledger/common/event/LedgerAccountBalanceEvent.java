package neo.ledger.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class LedgerAccountBalanceEvent {
    private String accountId;
    private BigDecimal balance;
}
