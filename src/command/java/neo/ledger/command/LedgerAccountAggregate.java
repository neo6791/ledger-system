package neo.ledger.command;

import lombok.Data;
import neo.ledger.common.event.LedgerAccountBalanceEvent;
import neo.ledger.common.event.LedgerEntryEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Aggregate
public class LedgerAccountAggregate {

    @AggregateIdentifier
    private String accountId;

    private ZonedDateTime entryDate;

    private BigDecimal balance = BigDecimal.ZERO;

    private String errorMsg;

    public LedgerAccountAggregate() {
    }

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    protected void on(LedgerEntryCommand ledgerEntryCommand) {
        AggregateLifecycle.apply(new LedgerEntryEvent(ledgerEntryCommand.accountId(),
                ledgerEntryCommand.entryDate(),
                ledgerEntryCommand.description(),
                ledgerEntryCommand.amount(),
                ledgerEntryCommand.type()));
    }

    @EventSourcingHandler
    protected void on(LedgerEntryEvent ledgerEntryEvent) {

        this.accountId = ledgerEntryEvent.getAccountId();
        this.entryDate = ledgerEntryEvent.getEntryDate();

        if ("DR".equalsIgnoreCase(ledgerEntryEvent.getType())) {
            this.balance = this.balance.add(ledgerEntryEvent.getAmount());
        } else {
            this.balance = this.balance.subtract(ledgerEntryEvent.getAmount());
        }

        AggregateLifecycle.apply(new LedgerAccountBalanceEvent(this.accountId,
                this.balance));
    }
}
