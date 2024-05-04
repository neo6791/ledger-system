package neo.ledger.command.service;

import neo.ledger.command.controller.dto.LedgerEntryDTO;

import java.util.concurrent.CompletableFuture;

public interface LedgerCommandService {

    CompletableFuture<String> createLedgerAccountEntry(String accountId, LedgerEntryDTO ledgerEntryDTO);

}
