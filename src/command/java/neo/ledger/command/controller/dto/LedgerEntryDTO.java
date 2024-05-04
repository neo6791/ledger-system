package neo.ledger.command.controller.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record LedgerEntryDTO(ZonedDateTime entryDate, String description, BigDecimal amount,
                             String type) {

}
