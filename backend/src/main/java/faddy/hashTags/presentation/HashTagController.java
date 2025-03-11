package faddy.hashTags.presentation;

import faddy.hashTags.domain.dto.request.HashTagRequestDto;
import faddy.hashTags.domain.dto.response.HashTagIdResponseDto;
import faddy.hashTags.service.HashTagService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/hashTags")
public class HashTagController {

    private final HashTagService hashTagService;

    @ApiOperation(value = "다중 태그 입력 시 태그 list를 받아와 서버에 저장", notes = "저장 후 tag id list를 클라이언트에 반환")
    @PostMapping()
    public ResponseEntity<List<HashTagIdResponseDto>> saveTags(@RequestBody HashTagRequestDto tagRequest) {

        List<HashTagIdResponseDto> savedTags = hashTagService.saveTags(tagRequest);
        return ResponseEntity.ok().body(savedTags);
    }
}
