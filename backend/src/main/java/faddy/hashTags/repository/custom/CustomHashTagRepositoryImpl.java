package faddy.hashTags.repository.custom;

import faddy.hashTags.domain.HashTag;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class CustomHashTagRepositoryImpl implements CustomHashTagRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<HashTag> saveAll(List<HashTag> hashTags) {
        List<HashTag> savedHashTags = new ArrayList<>();
        for (HashTag hashTag : hashTags) {
            savedHashTags.add(entityManager.merge(hashTag));
        }
        return savedHashTags;
    }
}