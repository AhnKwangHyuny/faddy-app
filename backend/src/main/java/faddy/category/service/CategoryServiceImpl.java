package faddy.category.service;

import faddy.category.domain.Category;
import faddy.category.domain.CategoryName;
import faddy.category.repository.CategoryRepository;
import faddy.global.exception.BadRequestException;
import faddy.type.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 해당 이름과 콘텐츠 타입의 카테고리가 존재하는지 확인
     *
     * @param name        카테고리 이름
     * @param contentType 콘텐츠 타입
     */
    @Override
    public void ensureCategoryExists(String name, ContentType contentType) {
        CategoryName categoryName;
        try {
            categoryName = CategoryName.fromValue(name ,contentType);
            if (categoryName == null) {
                throw new BadRequestException(9200, "Invalid category name: " + name);
            }

        } catch (IllegalArgumentException e) {
            throw new BadRequestException(9200, "Invalid category name: " + name);
        }
    }

    /**
     * 부모-자식 카테고리를 생성하고 연결하며, 생성된 부모-자식 카테고리 ID 쌍을 반환
     *
     * @param categoryPairs 부모-자식 카테고리 이름 쌍
     * @param contentType   콘텐츠 타입
     * @return 생성된 부모-자식 카테고리 ID 쌍
     */
    @Override
    @Transactional
    public Map<Long, Long> createAndLinkCategories(Map<String, String> categoryPairs, ContentType contentType) {
        Map<Long, Long> linkedCategories = new HashMap<>();

        for (Map.Entry<String, String> entry : categoryPairs.entrySet()) {

            String parentName = entry.getKey();
            String childName = entry.getValue();

            System.out.println("parentName = " + parentName + "  childName  = " + childName );

            // 부모 카테고리의 유효성을 확인
            ensureCategoryExists(parentName, contentType);

            // 부모 카테고리 저장
            Category parentCategory = saveParentCategory(parentName, contentType);

            // 자식 카테고리 저장
            Category childCategory = saveChildCategory(childName, parentCategory, contentType);



            // 생성된 부모와 자식 카테고리의 ID를 맵에 추가
            linkedCategories.put(parentCategory.getId(), childCategory.getId());
        }
        return linkedCategories;
    }

    /**
     * 카테고리를 생성하고 저장
     *
     * @param categoryName 카테고리 이름
     * @param parent        부모 카테고리 (없을 경우 null)
     * @param contentType   콘텐츠 타입
     * @return 저장된 카테고리
     */
    @Override
    public Category saveCategory(String categoryName, Category parent, ContentType contentType) {
        Category category = new Category(categoryName, parent, contentType);
        return categoryRepository.save(category);
    }

    /**
     * 자식 카테고리를 생성하고 저장
     *
     * @param childCategoryName 자식 카테고리 이름
     * @param parentCategory    부모 카테고리
     * @param contentType       콘텐츠 타입
     * @return 저장된 자식 카테고리
     */
    @Override
    public Category saveChildCategory(String childCategoryName, Category parentCategory, ContentType contentType) {
        Category childCategory = new Category(childCategoryName, parentCategory, contentType);
        return categoryRepository.save(childCategory);
    }

    /**
     * 부모 카테고리를 생성하고 저장
     *
     * @param parentCategoryName 부모 카테고리 이름
     * @param contentType        콘텐츠 타입
     * @return 저장된 부모 카테고리
     */
    @Override
    public Category saveParentCategory(String parentCategoryName, ContentType contentType) {
        // 부모 카테고리가 이미 존재하는지 확인
//        Optional<Category> existingCategory = categoryRepository.findByNameAndContentType(parentCategoryName, contentType);
//        if (existingCategory.isPresent()) {
//            return existingCategory.get();
//        }

        // 부모 카테고리가 존재하지 않는 경우 새로 생성
        Category category = new Category(parentCategoryName, null, contentType);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findByIdsIn(List<Long> ids) {

        List<Category> categories = categoryRepository.findByIdIn(ids);

        if(categories.size() != ids.size()) {
            throw new BadRequestException(400 , "존재하지 않는 카테고리 id가 있습니다.");
        }

        return  categories;
    }
}