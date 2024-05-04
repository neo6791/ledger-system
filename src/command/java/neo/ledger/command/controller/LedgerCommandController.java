package neo.ledger.command.controller;

import neo.ledger.command.controller.dto.LedgerEntryDTO;
import neo.ledger.command.service.LedgerCommandService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/ledger")
public class LedgerCommandController {

    private final LedgerCommandService ledgerCommandService;

    public LedgerCommandController(LedgerCommandService ledgerCommandService) {
        this.ledgerCommandService = ledgerCommandService;
    }

    @PutMapping(value = "/account/{accountId}/entry")
    public CompletableFuture<String> postLedgerEntry(@PathVariable(value = "accountId") String accountId,
                                                     @RequestBody LedgerEntryDTO ledgerEntryDTO) {
        return ledgerCommandService.createLedgerAccountEntry(accountId, ledgerEntryDTO);
    }
}
