package finwiz.app.seed;

import finwiz.app.entity.currency.Currency;
import finwiz.app.repository.currency.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CurrencySeed implements CommandLineRunner {

    private final CurrencyRepository currencyRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (currencyRepository.count() > 0) {
            return; // idempotent: не заливаем повторно
        }

        List<Currency> currencies = List.of(
            Currency.builder().code("RUB").name("Russian Ruble").minorUnits(2).symbol("₽").build(),
            Currency.builder().code("USD").name("US Dollar").minorUnits(2).symbol("$").build(),
            Currency.builder().code("EUR").name("Euro").minorUnits(2).symbol("€").build(),
            Currency.builder().code("CNY").name("Chinese Yuan").minorUnits(2).symbol("¥").build(),
            Currency.builder().code("GBP").name("British Pound").minorUnits(2).symbol("£").build(),
            Currency.builder().code("JPY").name("Japanese Yen").minorUnits(0).symbol("¥").build()
        );

        currencyRepository.saveAll(currencies);
    }
}
