package ibu.master.newsly.rest.controller;

import ibu.master.newsly.core.model.News;
import ibu.master.newsly.core.service.NewsService;
import ibu.master.newsly.rest.dto.NewsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable Long id) {
        News news = newsService.getById(id);
        NewsDTO newsDTO = NewsDTO.fromNews(news); // Convert to DTO for response
        return ResponseEntity.ok(newsDTO);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NewsDTO>> getAllNews() {
        List<NewsDTO> newsList = newsService.getAllNews()
                .stream()
                .map(NewsDTO::fromNews) // Convert entities to DTOs
                .collect(Collectors.toList());
        return ResponseEntity.ok(newsList);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/category/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NewsDTO>> getNewsByCategoryId(@PathVariable Long categoryId) {
        List<NewsDTO> newsList = newsService.getNewsByCategoryId(categoryId)
                .stream()
                .map(NewsDTO::fromNews) // Convert entities to DTOs
                .collect(Collectors.toList());
        return ResponseEntity.ok(newsList);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NewsDTO>> getNewsByUserId(@PathVariable Long userId) {
        List<NewsDTO> newsList = newsService.getNewsByUserId(userId)
                .stream()
                .map(NewsDTO::fromNews) // Convert entities to DTOs
                .collect(Collectors.toList());
        return ResponseEntity.ok(newsList);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewsDTO> createNews(@RequestBody NewsDTO newsDTO) {
        try {
            News createdNews = newsService.createNews(newsDTO);
            return new ResponseEntity<>(NewsDTO.fromNews(createdNews), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @RequestMapping(method = RequestMethod.PUT, path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewsDTO> updateNews(@PathVariable Long id, @RequestBody NewsDTO newsDTO) {
        News updatedNews = newsService.updateNews(id, newsDTO); // Use NewsDTO directly
        return ResponseEntity.ok(NewsDTO.fromNews(updatedNews)); // Convert result to DTO
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
