package faddy.category.repository;

import faddy.category.domain.Category;
import faddy.type.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category , Long> {
    Optional<Category> findByName(String name);

    boolean existsByNameAndContentType(String name, ContentType contentType);

    Optional<Category> findByNameAndContentType(String name, ContentType contentType);

    Category save(Category category);

    List<Category> findByIdIn(List<Long> ids);
}
