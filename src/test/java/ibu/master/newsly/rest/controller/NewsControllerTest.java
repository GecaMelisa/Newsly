package ibu.master.newsly.rest.controller;

import ibu.master.newsly.core.model.News;
import ibu.master.newsly.core.service.NewsService;
import ibu.master.newsly.rest.dto.NewsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class NewsControllerTest {

    @Mock
    private NewsService newsService;

    private MockMvc mockMvc;

    @Autowired
    private NewsController newsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        newsController = new NewsController(newsService);
        mockMvc = MockMvcBuilders.standaloneSetup(newsController).build();
    }

    @Test
    void testGetNewsById_Success() throws Exception {
        // Given
        News news = new News();
        news.setId(1L);
        news.setTitle("Test News");
        news.setContent("This is a test news article.");
        NewsDTO newsDTO = NewsDTO.fromNews(news);

        when(newsService.getById(1L)).thenReturn(news);

        // When & Then
        mockMvc.perform(get("/api/news/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test News")))
                .andExpect(jsonPath("$.content", is("This is a test news article.")));

        verify(newsService, times(1)).getById(1L);
    }

    @Test
    void testGetAllNews_Success() throws Exception {
        // Given
        News news1 = new News();
        news1.setId(1L);
        news1.setTitle("News 1");

        News news2 = new News();
        news2.setId(2L);
        news2.setTitle("News 2");

        when(newsService.getAllNews()).thenReturn(Arrays.asList(news1, news2));

        // When & Then
        mockMvc.perform(get("/api/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("News 1")))
                .andExpect(jsonPath("$[1].title", is("News 2")));

        verify(newsService, times(1)).getAllNews();
    }

    @Test
    void testCreateNews_Success() throws Exception {
        // Given
        News news = new News();
        news.setId(1L);
        news.setTitle("Test News");

        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitle("Test News");
        newsDTO.setContent("This is a test content.");

        when(newsService.createNews(any(NewsDTO.class))).thenReturn(news);

        // When & Then
        mockMvc.perform(post("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "title": "Test News",
                            "content": "This is a test content.",
                            "date": "29.12.2024.",
                            "categoryName": "Technology",
                            "userId": 1
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test News")));

        verify(newsService, times(1)).createNews(any(NewsDTO.class));
    }

    @Test
    void testUpdateNews_Success() throws Exception {
        // Given
        News updatedNews = new News();
        updatedNews.setId(1L);
        updatedNews.setTitle("Updated News");

        NewsDTO updatedNewsDTO = new NewsDTO();
        updatedNewsDTO.setTitle("Updated News");
        updatedNewsDTO.setContent("Updated content.");

        when(newsService.updateNews(eq(1L), any(NewsDTO.class))).thenReturn(updatedNews);

        // When & Then
        mockMvc.perform(put("/api/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "title": "Updated News",
                            "content": "Updated content.",
                            "date": "29.12.2024.",
                            "categoryName": "Technology",
                            "userId": 1
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated News")));

        verify(newsService, times(1)).updateNews(eq(1L), any(NewsDTO.class));
    }

    @Test
    void testDeleteNews_Success() throws Exception {
        // Given
        doNothing().when(newsService).deleteNews(1L);

        // When & Then
        mockMvc.perform(delete("/api/news/1"))
                .andExpect(status().isOk());

        verify(newsService, times(1)).deleteNews(1L);
    }
}
