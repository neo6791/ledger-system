package neo.ledger.query.controller;

import neo.ledger.query.controller.dto.LedgerAccountBalanceDTO;
import neo.ledger.query.controller.dto.LedgerAccountBalanceOnDateDTO;
import neo.ledger.query.service.LedgerQueryService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/ledger")
public class AccountQueryController {
    private final LedgerQueryService ledgerQueryService;

    public AccountQueryController(LedgerQueryService ledgerQueryService) {
        this.ledgerQueryService = ledgerQueryService;
    }

    @GetMapping("account/{accountId}/balance")
    public LedgerAccountBalanceDTO readLedgerAccountBalance(@PathVariable(value = "accountId") String accountId) {
        return ledgerQueryService.readLedgerAccountBalance(accountId);
    }

    @PostMapping("account/{accountId}/balance")
    public BigDecimal readLedgerAccountBalance(@PathVariable(value = "accountId") String accountId,
                                               @RequestBody LedgerAccountBalanceOnDateDTO ledgerAccountBalanceOnDateDTO) {
        return ledgerQueryService.readLedgerAccountBalanceOn(accountId, ledgerAccountBalanceOnDateDTO.balanceAsAt());
    }

    @GetMapping("/{accountId}/events")
    public List<Object> listEventsForAccount(@PathVariable(value = "accountId") String accountId) {
        return ledgerQueryService.listEventsForAccount(accountId);
    }
}
