package faddy.hashTags.repository.custom;

import faddy.hashTags.domain.HashTag;

import java.util.List;

public interface CustomHashTagRepository {
    List<HashTag> saveAll(List<HashTag> hashTags);
}