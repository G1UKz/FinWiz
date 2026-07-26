package finwiz.app.service.category;

import finwiz.app.dto.category.CategoryResponse;
import finwiz.app.dto.category.CreateCategoryRequest;
import finwiz.app.entity.category.Category;
import finwiz.app.entity.user.User;
import finwiz.app.exception.ResourceNotFoundException;
import finwiz.app.repository.category.CategoryRepository;
import finwiz.app.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public CategoryResponse createCategory(Long userId, CreateCategoryRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = Category.builder()
                .user(user)
                .name(request.name())
                .type(request.type())
                .color(request.color())
                .icon(request.icon())
                .isSystem(false)
                .build();

        return toResponse(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> listCategories(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        List<Category> system = categoryRepository.findByIsSystemTrue();
        List<Category> userCategories = categoryRepository.findByUserId(userId);

        List<Category> combined = new ArrayList<>(system.size() + userCategories.size());
        combined.addAll(system);
        combined.addAll(userCategories);
        combined.sort(Comparator.comparing(Category::getName));

        return combined.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return toResponse(category);
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getType(),
                category.getColor(),
                category.getIcon(),
                category.getIsSystem(),
                category.getCreatedAt()
        );
    }
}
