package faddy.category.presentation;

import faddy.category.domain.dto.CategoryPairDto;
import faddy.category.domain.dto.response.CategoryIdPairDto;
import faddy.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 카테고리 관리 컨트롤러.
 * 콘텐츠 카테고리의 생성, 연결 및 관리 담당.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 콘텐츠에서 선택한 카테고리 저장 및 부모-자식 카테고리 관계 설정.
     *
     * @Description
     * 클라이언트로부터 전달받은 카테고리 페어 정보 처리.
     * 카테고리 엔티티 생성 및 DB 저장.
     * 부모-자식 관계 설정 및 매핑 구현.
     * 생성된 카테고리 ID 매핑 정보 반환.
     *
     * @Request
     * - contentType: 콘텐츠 유형 (필수)
     * - categoryPairs: 카테고리 페어 목록 (부모-자식 관계 정보)
     *
     * @Response
     * - contentType: 요청된 콘텐츠 유형
     * - linkedCategoryPairSet: 생성된 카테고리 ID 매핑 정보 목록
     *
     * @Error
     * - 400: 유효하지 않은 요청 파라미터
     * - 500: 서버 내부 오류
     *
     * @param categoryPairs 카테고리 페어 정보가 담긴 DTO 객체
     * @return 생성된 카테고리 ID 매핑 정보
     */
    @PostMapping
    public ResponseEntity<CategoryIdPairDto> saveCategories(@RequestBody @Valid CategoryPairDto categoryPairs) {
        log.debug("카테고리 생성 및 연결 요청 수신: {}", categoryPairs);

        CategoryIdPairDto response = new CategoryIdPairDto();
        response.setContentType(categoryPairs.getContentType());

        for (Map<String, String> categoryPair : categoryPairs.getCategoryPairs()) {
            Map<Long, Long> idPair = categoryService.createAndLinkCategories(categoryPair, categoryPairs.getContentType());
            response.getLinkedCategoryPairSet().add(idPair);
        }

        log.info("카테고리 페어 생성 및 연결 완료. 콘텐츠 타입: {}, 생성된 페어 수: {}",
                categoryPairs.getContentType(), response.getLinkedCategoryPairSet().size());

        return ResponseEntity.ok().body(response);
    }
}