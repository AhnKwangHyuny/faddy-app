package faddy.category.service;

import faddy.category.domain.Category;
import faddy.type.ContentType;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    /**
     * 해당 이름과 콘텐츠 타입의 카테고리가 존재하는지 확인
     * @param name 카테고리 이름
     * @param contentType 콘텐츠 타입
     */
    void ensureCategoryExists(String name, ContentType contentType);

    /**
     * 부모-자식 카테고리를 생성하고 연결하며, 생성된 부모-자식 카테고리 ID 쌍을 반환
     * @param categoryPairs 부모-자식 카테고리 이름 쌍
     * @param contentType 콘텐츠 타입
     * @return 생성된 부모-자식 카테고리 ID 쌍
     */
    Map<Long, Long> createAndLinkCategories(Map<String, String> categoryPairs, ContentType contentType);

    /**
     * 자식 카테고리를 생성하고 저장
     * @param childCategoryName 자식 카테고리 이름
     * @param parentCategory 부모 카테고리
     * @param contentType 콘텐츠 타입
     * @return 저장된 자식 카테고리
     */
    Category saveChildCategory(String childCategoryName, Category parentCategory, ContentType contentType);

    /**
     * 카테고리를 생성하고 저장
     * @param categoryName 카테고리 이름
     * @param parent 부모 카테고리 (없을 경우 null)
     * @param contentType 콘텐츠 타입
     * @return 저장된 카테고리
     */
    Category saveCategory(String categoryName, Category parent, ContentType contentType);

    /**
     * 부모 카테고리를 생성하고 저장
     * @param parentCategoryName 부모 카테고리 이름
     * @param contentType 콘텐츠 타입
     * @return 저장된 부모 카테고리
     */
    Category saveParentCategory(String parentCategoryName, ContentType contentType);

    /**
     * ids로 category entities 반환
     * @param List<Long> 카테고리 아이디 셋
     * @return 아이디에 해당하는 카테고리 리스트
     */

    List<Category> findByIdsIn(List<Long> ids);
}
