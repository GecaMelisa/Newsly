package ibu.master.newsly.core.service;

import ibu.master.newsly.core.api.generateCategory.CategoryGenerator;
import ibu.master.newsly.core.model.Category;
import ibu.master.newsly.core.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryGenerator categoryGenerator;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryService(categoryRepository, categoryGenerator);
    }

    @Test
    void testGenerateCategory() {
        // Given
        String content = "This is a technology-related content.";
        String expectedCategory = "Technology";

        when(categoryGenerator.generateCategory(content)).thenReturn(expectedCategory);

        // When
        String generatedCategory = categoryService.generateCategory(content);

        // Then
        assertEquals(expectedCategory, generatedCategory, "Generated category should match expected value");
        verify(categoryGenerator, times(1)).generateCategory(content);
    }

    @Test
    void testCreateCategory() {
        // Given
        Category category = new Category();
        category.setName("Technology");

        when(categoryRepository.save(category)).thenReturn(category);

        // When
        Category createdCategory = categoryService.createCategory(category);

        // Then
        assertNotNull(createdCategory, "Created category should not be null");
        assertEquals("Technology", createdCategory.getName(), "Category name should match");
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testGetAllCategories() {
        // Given
        Category category1 = new Category();
        category1.setName("Technology");

        Category category2 = new Category();
        category2.setName("Health");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        // When
        List<Category> categories = categoryService.getAllCategories();

        // Then
        assertEquals(2, categories.size(), "Should return 2 categories");
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoryById_CategoryExists() {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Technology");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        Optional<Category> foundCategory = categoryService.getCategoryById(categoryId);

        // Then
        assertTrue(foundCategory.isPresent(), "Category should be found");
        assertEquals("Technology", foundCategory.get().getName(), "Category name should match");
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void testGetCategoryById_CategoryDoesNotExist() {
        // Given
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When
        Optional<Category> foundCategory = categoryService.getCategoryById(categoryId);

        // Then
        assertFalse(foundCategory.isPresent(), "Category should not be found");
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void testGetCategoryByName_CategoryExists() {
        // Given
        String categoryName = "Technology";
        Category category = new Category();
        category.setName(categoryName);

        when(categoryRepository.findCategoryByName(categoryName)).thenReturn(category);

        // When
        Optional<Category> foundCategory = categoryService.getCategoryByName(categoryName);

        // Then
        assertTrue(foundCategory.isPresent(), "Category should be found");
        assertEquals("Technology", foundCategory.get().getName(), "Category name should match");
        verify(categoryRepository, times(1)).findCategoryByName(categoryName);
    }

    @Test
    void testGetCategoryByName_CategoryDoesNotExist() {
        // Given
        String categoryName = "Nonexistent";

        when(categoryRepository.findCategoryByName(categoryName)).thenReturn(null);

        // When
        Optional<Category> foundCategory = categoryService.getCategoryByName(categoryName);

        // Then
        assertFalse(foundCategory.isPresent(), "Category should not be found");
        verify(categoryRepository, times(1)).findCategoryByName(categoryName);
    }

    @Test
    void testUpdateCategory() {
        // Given
        Long categoryId = 1L;
        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Old Name");

        Category updatedDetails = new Category();
        updatedDetails.setName("Updated Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Category updatedCategory = categoryService.updateCategory(categoryId, updatedDetails);

        // Then
        assertNotNull(updatedCategory, "Updated category should not be null");
        assertEquals("Updated Name", updatedCategory.getName(), "Category name should be updated");
        verify(categoryRepository, times(1)).save(existingCategory);
    }

    @Test
    void testDeleteCategory() {
        // Given
        Long categoryId = 1L;
        doNothing().when(categoryRepository).deleteById(categoryId);

        // When
        categoryService.deleteCategory(categoryId);

        // Then
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}
