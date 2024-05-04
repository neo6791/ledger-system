package neo.ledger.command.service;

import neo.ledger.command.LedgerEntryCommand;
import neo.ledger.command.controller.dto.LedgerEntryDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class LedgerCommandServiceImpl implements LedgerCommandService {

    private final CommandGateway commandGateway;

    public LedgerCommandServiceImpl(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Override
    public CompletableFuture<String> createLedgerAccountEntry(String accountId, LedgerEntryDTO ledgerEntryDTO) {
        return commandGateway.send(new LedgerEntryCommand(
                accountId,
                ledgerEntryDTO.entryDate(),
                ledgerEntryDTO.description(),
                ledgerEntryDTO.amount(),
                ledgerEntryDTO.type()));
    }
}
