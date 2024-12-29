package ibu.master.newsly.rest.dto;

import ibu.master.newsly.core.model.Category;
import ibu.master.newsly.core.model.News;
import ibu.master.newsly.core.model.User;

import java.time.LocalDateTime;

public class NewsDTO {
    private String title;
    private String content;
    private String date;
    private String category_name;
    private Long user_Id;
    private String email;
    private String userName; // Added userName field

    // Default constructor
    public NewsDTO() {}

    // Parameterized constructor for output
    public NewsDTO(Long id, String title, String content, String date, String category_name, Long user_Id, String email, String userName) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.category_name = category_name;
        this.user_Id = user_Id;
        this.email = email;
        this.userName = userName; // Properly set the userName
    }

    // Static method to convert News entity to NewsDTO (Output)
    public static NewsDTO fromNews(News news) {
        return new NewsDTO(
                news.getId(),
                news.getTitle(),
                news.getContent(),
                news.getDate() != null ? news.getDate().toString() : null,
                news.getCategory() != null ? news.getCategory().getName() : null,
                news.getUser() != null ? news.getUser().getId() : null,
                news.getUser() != null ? news.getUser().getEmail() : null,
                news.getUser() != null ? news.getUser().getName() : null // Include user's name
        );
    }

    // Getters and Setters

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

    public String getCategoryName() {
        return category_name;
    }

    public void setCategoryName(String categoryName) {
        this.category_name = categoryName;
    }

    public Long getUserId() {
        return user_Id;
    }

    public void setUserId(Long userId) {
        this.user_Id = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
