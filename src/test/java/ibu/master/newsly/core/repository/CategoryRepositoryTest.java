package ibu.master.newsly.core.repository;

import ibu.master.newsly.core.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CategoryRepositoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Technology");
    }

    @Test
    void testFindCategoryByName_CategoryExists() {
        // Given
        String categoryName = "Technology";

        // Mock behavior
        when(categoryRepository.findCategoryByName(categoryName)).thenReturn(testCategory);

        // When
        Category result = categoryRepository.findCategoryByName(categoryName);

        // Then
        assertNotNull(result, "Category should not be null");
        assertEquals(testCategory.getId(), result.getId(), "Category ID should match");
        assertEquals(testCategory.getName(), result.getName(), "Category name should match");
    }

    @Test
    void testFindCategoryByName_CategoryDoesNotExist() {
        // Given
        String categoryName = "Nonexistent";

        // Mock behavior
        when(categoryRepository.findCategoryByName(categoryName)).thenReturn(null);

        // When
        Category result = categoryRepository.findCategoryByName(categoryName);

        // Then
        assertNull(result, "Category should be null");
    }
}
