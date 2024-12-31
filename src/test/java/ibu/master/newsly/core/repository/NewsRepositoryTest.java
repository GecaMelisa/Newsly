package ibu.master.newsly.core.repository;

import ibu.master.newsly.core.model.Category;
import ibu.master.newsly.core.model.News;
import ibu.master.newsly.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class NewsRepositoryTest {

    @Mock
    private NewsRepository newsRepository;

    private User testUser;
    private Category testCategory;
    private News testNews;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("testuser@example.com");

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Technology");

        testNews = new News();
        testNews.setId(1L);
        testNews.setTitle("Test News");
        testNews.setContent("This is a test news content.");
        testNews.setDate(new java.util.Date());
        testNews.setUser(testUser);
        testNews.setCategory(testCategory);
    }

    @Test
    void testFindByCategoryId() {
        // Given
        Long categoryId = 1L;
        List<News> newsList = new ArrayList<>();
        newsList.add(testNews);

        // Mock behavior
        when(newsRepository.findByCategoryId(categoryId)).thenReturn(newsList);

        // When
        List<News> result = newsRepository.findByCategoryId(categoryId);

        // Then
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Result size should match");
        assertEquals(testCategory, result.get(0).getCategory(), "Category should match");
    }

    @Test
    void testFindByUserId() {
        // Given
        Long userId = 1L;
        List<News> newsList = new ArrayList<>();
        newsList.add(testNews);

        // Mock behavior
        when(newsRepository.findByUserId(userId)).thenReturn(newsList);

        // When
        List<News> result = newsRepository.findByUserId(userId);

        // Then
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Result size should match");
        assertEquals(testUser, result.get(0).getUser(), "User should match");
    }
}
