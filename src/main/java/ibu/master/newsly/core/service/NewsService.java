package ibu.master.newsly.core.service;

import ibu.master.newsly.core.model.Category;
import ibu.master.newsly.core.model.News;
import ibu.master.newsly.core.model.User;
import ibu.master.newsly.core.repository.CategoryRepository;
import ibu.master.newsly.core.repository.NewsRepository;
import ibu.master.newsly.core.repository.UserRepository;
import ibu.master.newsly.rest.dto.NewsDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public NewsService(NewsRepository newsRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.newsRepository = newsRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public News getById(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + newsId));
    }

    public News createNews(NewsDTO newsDto) {
        News news = new News();
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());

        // Validate and assign Category (optional)
        if (newsDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(newsDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + newsDto.getCategoryId()));
            news.setCategory(category);
        }

        // Validate and assign the author (User)
        if (newsDto.getUserId() != null) {
            User user = userRepository.findById(newsDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + newsDto.getUserId()));
            news.setUser(user); // Set the author of the news
        } else {
            throw new RuntimeException("User ID is required to create news");
        }

        // Save News and return
        return newsRepository.save(news);
    }


    public News updateNews(Long id, NewsDTO newsDto) {
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + id));

        existingNews.setTitle(newsDto.getTitle());
        existingNews.setContent(newsDto.getContent());

        Optional<Category> categoryOptional = categoryRepository.findById(newsDto.getCategoryId());
        categoryOptional.ifPresent(existingNews::setCategory);

        Optional<User> userOptional = userRepository.findById(newsDto.getUserId());
        userOptional.ifPresent(existingNews::setUser);

        return newsRepository.save(existingNews);
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }


    public List<News> getNewsByCategoryId(Long categoryId) {
        return newsRepository.findByCategoryId(categoryId);
    }

    public List<News> getNewsByUserId(Long userId) {
        return newsRepository.findByUserId(userId);
    }
}
