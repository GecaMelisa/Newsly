package ibu.master.newsly.core.service;

import ibu.master.newsly.core.api.generateCategory.CategoryGenerator;
import ibu.master.newsly.core.model.Category;
import ibu.master.newsly.core.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryGenerator categoryGenerator;


    // Constructor injection
    public CategoryService(CategoryRepository categoryRepository, CategoryGenerator categoryGenerator) {
        this.categoryRepository = categoryRepository;
        this.categoryGenerator = categoryGenerator;
    }
    public String generateCategory (String content) {
        return categoryGenerator.generateCategory(content);
    }
    // Create a new category
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Retrieve all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Retrieve a category by ID
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Retrieve a category by name
    public Optional<Category> getCategoryByName(String name) {
        return Optional.ofNullable(categoryRepository.findCategoryByName(name));
    }

    // Update a category
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(categoryDetails.getName());
        return categoryRepository.save(category);
    }

    // Delete a category by ID
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
