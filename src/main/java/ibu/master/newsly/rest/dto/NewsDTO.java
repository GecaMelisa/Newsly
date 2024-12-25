package ibu.master.newsly.rest.dto;

import ibu.master.newsly.core.model.Category;
import ibu.master.newsly.core.model.News;
import ibu.master.newsly.core.model.User;

import java.time.LocalDateTime;

public class NewsDTO {

    private Long id;
    private String title;
    private String content;
    private String date;
    private Long categoryId;
    private String categoryName;
    private Long userId;

    // Default constructor
    public NewsDTO() {
    }

    // Parameterized constructor for output
    public NewsDTO(Long id, String title, String content, String date, Long categoryId, String categoryName, Long userId, String userName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.categoryId = categoryId;
        this.userId = userId;
    }

    // Static method to convert News entity to NewsDTO (Output)
    public static NewsDTO fromNews(News news) {
        return new NewsDTO(
                news.getId(),
                news.getTitle(),
                news.getContent(),
                news.getDate().toString(),
                news.getCategory() != null ? news.getCategory().getId() : null,
                news.getCategory() != null ? news.getCategory().getName() : null,
                news.getUser() != null ? news.getUser().getId() : null, // User ID
                news.getUser() != null ? news.getUser().getName() : null //
        );
    }



    // Method to convert NewsDTO to News entity (Input)
    public News toNews(Category category, User user) {
        News news = new News();
        news.setId(this.id);
        news.setTitle(this.title);
        news.setContent(this.content);
        news.setDate(LocalDateTime.parse(this.date)); // Adjust parsing if needed
        news.setCategory(category);
        news.setUser(user);
        return news;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}
