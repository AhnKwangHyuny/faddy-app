package faddy.styleBoard.repository;

import faddy.styleBoard.domain.Category;
import faddy.styleBoard.domain.StyleBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StyleBoardJpaRepository extends JpaRepository<StyleBoard, Long> {

    Optional<StyleBoard> findById(Long id);

    @EntityGraph(attributePaths = {"author", "author.profile"})
    @Query("SELECT sb FROM StyleBoard sb WHERE sb.id = :styleBoardId")
    Optional<StyleBoard> findByIdWithAuthorAndProfile(@Param("styleBoardId") Long styleBoardId);

    @Query("SELECT CASE " +
            "WHEN COUNT(sb) > 0 " +
            "THEN TRUE ELSE FALSE " +
            "END FROM StyleBoard sb " +
            "WHERE sb.id = :styleBoardId AND sb.author.id = :userId")
    boolean existsByIdAndUserId(@Param("styleBoardId") Long styleBoardId, @Param("userId") Long userId);

    @Query("SELECT sb FROM StyleBoard sb")
    List<StyleBoard> findAll();

    @Query("SELECT sb FROM StyleBoard sb WHERE sb.category = :category")
    Page<StyleBoard> findByCategory(@Param("category") Category category, Pageable pageable);

    @Query("SELECT sb FROM StyleBoard sb WHERE sb.category = :category AND (SELECT COUNT(ht) FROM HashTag ht WHERE ht.styleBoard = sb AND ht.name IN :tags) = :tagSize")
    Page<StyleBoard> findByCategoryAndTagsIn(@Param("category") Category category, @Param("tags") List<String> tags, @Param("tagSize") long tagSize, Pageable pageable);

    @Query("SELECT sb FROM StyleBoard sb WHERE EXISTS (SELECT ht FROM HashTag ht WHERE ht.styleBoard = sb AND ht.name IN :tags)")
    Page<StyleBoard> findByTagsIn(@Param("tags") List<String> tags, Pageable pageable);

    @Query("SELECT sb FROM StyleBoard sb ORDER BY sb.created_at DESC")
    Page<StyleBoard> findAllStyleBoardsSortedByCreationDateDesc(Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM StyleBoard sb WHERE sb.id = :styleBoardId")
    void deleteById(@Param("styleBoardId") Long styleBoardId);
}
