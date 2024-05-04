package neo.ledger.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedgerEntryEvent {
    private String accountId;
    private ZonedDateTime entryDate;
    private String description;
    private BigDecimal amount;
    private String type;
}
