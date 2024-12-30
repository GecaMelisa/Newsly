package ibu.master.newsly.core.repository;

import ibu.master.newsly.core.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    // Custom query to find all news by category ID
    List<News> findByCategoryId(Long categoryId);

    // Custom query to find all news by user ID
    List<News> findByUserId(Long userId);


}
