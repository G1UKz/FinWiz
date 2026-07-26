package finwiz.app.repository.transaction;

import finwiz.app.entity.bankaccount.AccountType;
import finwiz.app.entity.bankaccount.BankAccount;
import finwiz.app.entity.category.Category;
import finwiz.app.entity.category.CategoryType;
import finwiz.app.entity.currency.Currency;
import finwiz.app.entity.transaction.Transaction;
import finwiz.app.entity.transaction.TransactionType;
import finwiz.app.entity.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private TransactionRepository repo;

    private User persistUser() {
        User u = new User();
        u.setEmail("tx-test@finwiz.dev");
        u.setPasswordHash("$2a$10$hash");
        u.setName("Tx Tester");
        return em.persistAndFlush(u);
    }

    private BankAccount persistAccount(User user) {
        Currency rub = new Currency("RUB", "Rub", 2, "₽");
        em.persist(rub);
        BankAccount acc = new BankAccount();
        acc.setUser(user);
        acc.setName("Сбер");
        acc.setAccountType(AccountType.DEBIT);
        acc.setCurrency(rub);
        acc.setBalanceMinor(0L);
        acc.setIsActive(true);
        return em.persistAndFlush(acc);
    }

    private Category persistCategory() {
        Category c = new Category();
        c.setName("Продукты");
        c.setType(CategoryType.EXPENSE);
        c.setColor("#F44336");
        c.setIcon("cart");
        c.setIsSystem(true);
        return em.persistAndFlush(c);
    }

    @Test
    void shouldFindByUserId() {
        User user = persistUser();
        BankAccount acc = persistAccount(user);
        Category cat = persistCategory();

        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setBankAccount(acc);
        tx.setCategory(cat);
        tx.setType(TransactionType.EXPENSE);
        tx.setAmountMinor(345000L);
        tx.setDescription("Пятёрочка");
        tx.setTransactionDate(LocalDate.of(2026, 7, 26));
        em.persistAndFlush(tx);

        List<Transaction> found = repo.findByUserId(user.getId());
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getDescription()).isEqualTo("Пятёрочка");
    }

    @Test
    void shouldFindByBankAccountId() {
        User user = persistUser();
        BankAccount acc = persistAccount(user);
        Category cat = persistCategory();

        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setBankAccount(acc);
        tx.setCategory(cat);
        tx.setType(TransactionType.EXPENSE);
        tx.setAmountMinor(100000L);
        tx.setTransactionDate(LocalDate.of(2026, 7, 26));
        em.persistAndFlush(tx);

        List<Transaction> found = repo.findByBankAccountId(acc.getId());
        assertThat(found).hasSize(1);
    }
}
