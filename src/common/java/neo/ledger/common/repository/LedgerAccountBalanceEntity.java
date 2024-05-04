package neo.ledger.common.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "LEDGER_ACCOUNT_BALANCE")
public class LedgerAccountBalanceEntity {

    @Id
    @Column(name = "LAB_ID")
    private String id;

    @Column(name = "LAB_BALANCE")
    private BigDecimal balance;

}
