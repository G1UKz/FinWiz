package finwiz.app.seed;

import finwiz.app.entity.Category;
import finwiz.app.entity.enums.CategoryType;
import finwiz.app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategorySeed implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return;
        }

        List<Category> categories = List.of(
            Category.builder().name("Зарплата").type(CategoryType.INCOME).color("#4CAF50").icon("wallet").isSystem(true).build(),
            Category.builder().name("Подарки").type(CategoryType.INCOME).color("#2196F3").icon("gift").isSystem(true).build(),
            Category.builder().name("Прочие доходы").type(CategoryType.INCOME).color("#9E9E9E").icon("more_horiz").isSystem(true).build(),
            Category.builder().name("Продукты").type(CategoryType.EXPENSE).color("#F44336").icon("shopping_cart").isSystem(true).build(),
            Category.builder().name("Транспорт").type(CategoryType.EXPENSE).color("#FF9800").icon("directions_car").isSystem(true).build(),
            Category.builder().name("Жильё").type(CategoryType.EXPENSE).color("#795548").icon("home").isSystem(true).build(),
            Category.builder().name("Развлечения").type(CategoryType.EXPENSE).color("#E91E63").icon("movie").isSystem(true).build(),
            Category.builder().name("Здоровье").type(CategoryType.EXPENSE).color("#00BCD4").icon("local_hospital").isSystem(true).build(),
            Category.builder().name("Одежда").type(CategoryType.EXPENSE).color("#673AB7").icon("checkroom").isSystem(true).build(),
            Category.builder().name("Связь").type(CategoryType.EXPENSE).color("#3F51B5").icon("wifi").isSystem(true).build(),
            Category.builder().name("Прочие расходы").type(CategoryType.EXPENSE).color("#9E9E9E").icon("more_horiz").isSystem(true).build()
        );

        categoryRepository.saveAll(categories);
    }
}
