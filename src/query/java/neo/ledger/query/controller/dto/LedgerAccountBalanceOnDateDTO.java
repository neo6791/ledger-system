package neo.ledger.query.controller.dto;

import java.time.ZonedDateTime;

public record LedgerAccountBalanceOnDateDTO(ZonedDateTime balanceAsAt) {
}
