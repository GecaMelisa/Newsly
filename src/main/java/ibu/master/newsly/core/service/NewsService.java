package ibu.master.newsly.core.service;

import ibu.master.newsly.core.api.generateCategory.CategoryGenerator;
import ibu.master.newsly.core.model.Category;
import ibu.master.newsly.core.model.News;
import ibu.master.newsly.core.model.User;
import ibu.master.newsly.core.repository.CategoryRepository;
import ibu.master.newsly.core.repository.NewsRepository;
import ibu.master.newsly.core.repository.UserRepository;
import ibu.master.newsly.rest.dto.NewsDTO;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryGenerator categoryGenerator;

    DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy."); // Define custom date format


    public NewsService(NewsRepository newsRepository, CategoryRepository categoryRepository, UserRepository userRepository, CategoryGenerator categoryGenerator) {
        this.newsRepository = newsRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.categoryGenerator = categoryGenerator;
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

        LocalDate localDate = LocalDate.parse(newsDto.getDate(), customFormatter);
        Date date = Date.from(localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        news.setDate(date);


        Category category = categoryRepository.findCategoryByName(newsDto.getCategoryName());
        if(category != null) {
            news.setCategory(category);
        } else {
            Category newCategory = new Category();
            newCategory.setName(newsDto.getCategoryName());
        }


        Optional<User> user = userRepository.findById(newsDto.getUserId());
        user.ifPresent(news::setUser);

        return newsRepository.save(news);
    }




    public News updateNews(Long id, NewsDTO newsDto) {
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + id));

        existingNews.setTitle(newsDto.getTitle());
        existingNews.setContent(newsDto.getContent());

        Category category = categoryRepository.findCategoryByName(newsDto.getCategoryName());
        if(category != null) {
            existingNews.setCategory(category);
        } else {
            Category newCategory = new Category();
            newCategory.setName(newsDto.getCategoryName());
        }


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
