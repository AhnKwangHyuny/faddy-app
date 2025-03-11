package faddy.image.repository;

import faddy.image.domain.Image;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {

    Optional<Image> findByHashName(String hashName);

    @Transactional
    @Modifying
    @Query("DELETE FROM Image i WHERE i.hashName IN :hashNames")
    void deleteAllByHashNameIn(@Param("hashNames") List<String> hashNames);

    @Query(value = "SELECT * FROM images", nativeQuery = true)
    List<Image> findAll();

    @Query("SELECT i FROM Image i WHERE i.hashName IN :hashedNames")
    List<Image> findByHashedNameIn(@Param("hashedNames") List<String> hashedNames);

    @Query("SELECT i FROM Image i WHERE i.imageUrl IN :imageUrls")
    List<Image> findByImageUrlIn(@Param("imageUrls") List<String> imageUrls);

    @Query("SELECT i FROM Image i WHERE i.styleBoard.id = :styleBoardId")
    List<Image> findByStyleBoardId(@Param("styleBoardId") Long styleBoardId);
}
