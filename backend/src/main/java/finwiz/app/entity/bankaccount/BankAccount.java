package finwiz.app.entity.bankaccount;
import finwiz.app.entity.user.User;
import finwiz.app.entity.currency.Currency;

import finwiz.app.entity.bankaccount.AccountType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String bankName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType accountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_code", nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private Long balanceMinor;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(length = 255)
    private String note;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Transient
    public BigDecimal getBalance() {
        if (currency == null || currency.getMinorUnits() == null) {
            return BigDecimal.valueOf(balanceMinor).movePointLeft(2);
        }
        return BigDecimal.valueOf(balanceMinor).movePointLeft(currency.getMinorUnits());
    }
}
