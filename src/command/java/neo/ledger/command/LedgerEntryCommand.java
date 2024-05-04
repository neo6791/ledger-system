package neo.ledger.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

public class LedgerEntryCommand {
    @TargetAggregateIdentifier
    private final String accountId;
    private final ZonedDateTime entryDate;
    private final String description;
    private final BigDecimal amount;
    private final String type;

    public LedgerEntryCommand(String accountId, ZonedDateTime entryDate, String description,
                              BigDecimal amount, String type) {
        this.accountId = accountId;
        this.entryDate = entryDate;
        this.description = description;
        this.amount = amount;
        this.type = type;
    }

    @TargetAggregateIdentifier
    public String accountId() {
        return accountId;
    }

    public ZonedDateTime entryDate() {
        return entryDate;
    }

    public String description() {
        return description;
    }

    public BigDecimal amount() {
        return amount;
    }

    public String type() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LedgerEntryCommand) obj;
        return Objects.equals(this.accountId, that.accountId) &&
                Objects.equals(this.entryDate, that.entryDate) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.amount, that.amount) &&
                Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, entryDate, description, amount, type);
    }

    @Override
    public String toString() {
        return "LedgerEntryCommand[" +
                "accountId=" + accountId + ", " +
                "entryDate=" + entryDate + ", " +
                "description=" + description + ", " +
                "amount=" + amount + ", " +
                "type=" + type + ']';
    }
}
