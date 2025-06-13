package ibu.master.newsly.core.service;

import ibu.master.newsly.core.api.generateCategory.CategoryGenerator;
import ibu.master.newsly.core.model.Category;
import ibu.master.newsly.core.model.News;
import ibu.master.newsly.core.model.User;
import ibu.master.newsly.core.repository.CategoryRepository;
import ibu.master.newsly.core.repository.NewsRepository;
import ibu.master.newsly.core.repository.UserRepository;
import ibu.master.newsly.rest.dto.NewsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryGenerator categoryGenerator;

    private NewsService newsService;

    private final DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        newsService = new NewsService(newsRepository, categoryRepository, userRepository, categoryGenerator);
    }

    @Test
    void testGetAllNews() {
        // Given
        News news1 = new News();
        News news2 = new News();
        when(newsRepository.findAll()).thenReturn(Arrays.asList(news1, news2));

        // When
        List<News> newsList = newsService.getAllNews();

        // Then
        assertEquals(2, newsList.size(), "Should return 2 news items");
        verify(newsRepository, times(1)).findAll();
    }

    @Test
    void testGetById_NewsExists() {
        // Given
        News news = new News();
        news.setId(1L);
        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));

        // When
        News foundNews = newsService.getById(1L);

        // Then
        assertNotNull(foundNews, "News should be found");
        assertEquals(1L, foundNews.getId(), "ID should match");
        verify(newsRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NewsDoesNotExist() {
        // Given
        when(newsRepository.findById(1L)).thenReturn(Optional.empty());

        // Then
        assertThrows(RuntimeException.class, () -> newsService.getById(1L), "Should throw exception when news not found");
    }

    @Test
    void testCreateNews() {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitle("Test Title");
        newsDTO.setContent("Test Content");
        newsDTO.setDate("29.12.2024.");
        newsDTO.setCategoryName("Technology");
        newsDTO.setUserId(1L);

        User user = new User();
        user.setId(1L);
        Category category = new Category();
        category.setName("Technology");

        when(categoryRepository.findCategoryByName("Technology")).thenReturn(category);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(newsRepository.save(any(News.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        News createdNews = newsService.createNews(newsDTO);

        // Then
        assertNotNull(createdNews, "Created news should not be null");
        assertEquals("Test Title", createdNews.getTitle(), "Title should match");
        assertEquals("Technology", createdNews.getCategory().getName(), "Category should match");
        verify(newsRepository, times(1)).save(any(News.class));
    }

    @Test
    void testUpdateNews() {
        // Given
        News existingNews = new News();
        existingNews.setId(1L);
        existingNews.setTitle("Old Title");

        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitle("Updated Title");
        newsDTO.setContent("Updated Content");
        newsDTO.setDate("30.12.2024.");
        newsDTO.setCategoryName("Science");
        newsDTO.setUserId(1L);

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setName("Science");

        when(newsRepository.findById(1L)).thenReturn(Optional.of(existingNews));
        when(categoryRepository.findCategoryByName("Science")).thenReturn(category);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(newsRepository.save(any(News.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        News updatedNews = newsService.updateNews(1L, newsDTO);

        // Then
        assertNotNull(updatedNews, "Updated news should not be null");
        assertEquals("Updated Title", updatedNews.getTitle(), "Title should be updated");
        assertEquals("Science", updatedNews.getCategory().getName(), "Category should be updated");
        verify(newsRepository, times(1)).save(any(News.class));
    }

    @Test
    void testDeleteNews() {
        // Given
        Long newsId = 1L;
        doNothing().when(newsRepository).deleteById(newsId);

        // When
        newsService.deleteNews(newsId);

        // Then
        verify(newsRepository, times(1)).deleteById(newsId);
    }

    @Test
    void testGetNewsByCategoryId() {
        // Given
        Long categoryId = 1L;
        News news1 = new News();
        News news2 = new News();
        when(newsRepository.findByCategoryId(categoryId)).thenReturn(Arrays.asList(news1, news2));

        // When
        List<News> newsList = newsService.getNewsByCategoryId(categoryId);

        // Then
        assertEquals(2, newsList.size(), "Should return 2 news items");
        verify(newsRepository, times(1)).findByCategoryId(categoryId);
    }

    @Test
    void testGetNewsByUserId() {
        // Given
        Long userId = 1L;
        News news1 = new News();
        News news2 = new News();
        when(newsRepository.findByUserId(userId)).thenReturn(Arrays.asList(news1, news2));

        // When
        List<News> newsList = newsService.getNewsByUserId(userId);

        // Then
        assertEquals(2, newsList.size(), "Should return 2 news items");
        verify(newsRepository, times(1)).findByUserId(userId);
    }
}
