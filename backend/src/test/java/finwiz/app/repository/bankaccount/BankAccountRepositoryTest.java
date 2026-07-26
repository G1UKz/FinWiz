package finwiz.app.repository.bankaccount;

import finwiz.app.entity.bankaccount.BankAccount;
import finwiz.app.entity.bankaccount.AccountType;
import finwiz.app.entity.currency.Currency;
import finwiz.app.entity.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BankAccountRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BankAccountRepository repo;

    private User persistUser() {
        User u = new User();
        u.setEmail("acc-test@finwiz.dev");
        u.setPasswordHash("$2a$10$hash");
        u.setName("Account Tester");
        return em.persistAndFlush(u);
    }

    private Currency persistCurrency() {
        Currency c = new Currency();
        c.setCode("RUB");
        c.setName("Russian Ruble");
        c.setMinorUnits(2);
        c.setSymbol("₽");
        return em.persistAndFlush(c);
    }

    @Test
    void shouldSaveAndFindByUserId() {
        User user = persistUser();
        Currency rub = persistCurrency();

        BankAccount acc = new BankAccount();
        acc.setUser(user);
        acc.setName("Тинькофф");
        acc.setBankName("Тинькофф");
        acc.setAccountType(AccountType.DEBIT);
        acc.setCurrency(rub);
        acc.setBalanceMinor(125000L);
        acc.setIsActive(true);
        em.persistAndFlush(acc);

        List<BankAccount> found = repo.findByUserId(user.getId());
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("Тинькофф");
        assertThat(found.get(0).getBalanceMinor()).isEqualTo(125000L);
    }

    @Test
    void shouldReturnEmptyForUnknownUser() {
        List<BankAccount> found = repo.findByUserId(999L);
        assertThat(found).isEmpty();
    }
}
