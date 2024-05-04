package neo.ledger.integration.test.steps;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record LedgerEntryDTO(ZonedDateTime entryDate, String description, BigDecimal amount,
                             String type) {

}
