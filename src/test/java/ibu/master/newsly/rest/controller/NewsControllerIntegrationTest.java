package ibu.master.newsly.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ibu.master.newsly.core.model.Category;
import ibu.master.newsly.core.model.News;
import ibu.master.newsly.core.model.User;
import ibu.master.newsly.core.repository.CategoryRepository;
import ibu.master.newsly.core.repository.NewsRepository;
import ibu.master.newsly.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NewsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User testUser;
    private Category testCategory;

    @BeforeEach
    void setup() {
        newsRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        testUser = new User("test@example.com", "Test User", "test123");
        userRepository.save(testUser);

        testCategory = new Category("Test Category");
        categoryRepository.save(testCategory);
    }

    @Test
    void shouldCreateNewsSuccessfully() throws Exception {
        News news = new News("Test Title", "Test Content", testUser, testCategory);
        news.setDate(new Date());

        mockMvc.perform(post("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(news)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void shouldReturnListOfNews() throws Exception {
        News news = new News("Sample Title", "Sample Content", testUser, testCategory);
        news.setDate(new Date());
        newsRepository.save(news);

        mockMvc.perform(get("/api/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldUpdateNewsSuccessfully() throws Exception {
        News news = new News("Old Title", "Old Content", testUser, testCategory);
        news.setDate(new Date());
        News saved = newsRepository.save(news);

        saved.setTitle("Updated Title");

        mockMvc.perform(put("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void shouldDeleteNewsSuccessfully() throws Exception {
        News news = new News("Delete Me", "To be deleted", testUser, testCategory);
        news.setDate(new Date());
        News saved = newsRepository.save(news);

        mockMvc.perform(delete("/api/news/" + saved.getId()))
                .andExpect(status().isNoContent());
    }
}
