package ibu.master.newsly.core.repository;


import ibu.master.newsly.core.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Custom query to find a category by name
    Category findCategoryByName(String name);
}
