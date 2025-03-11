package faddy.styleBoard.utils;

import faddy.hashTags.dto.request.HashTagRequestDTO;
import faddy.hashTags.types.ContentType;
import faddy.image.dto.request.ImageLookupRequestDTO;
import faddy.styleBoard.dto.request.StyleBoardCreateDTO;
import faddy.styleBoard.dto.request.StyleBoardRequestDTO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StyleBoardRequestParser {

    public static List<ImageLookupRequestDTO> extractImageLookupRequestDTOs(String content) {

        List<String> imageUrls = ContentParser.extractImageUrls(content);

        // 이미지 URL이 있다면 ImageLookupRequestDTO로 변환 없으면 null 반환
        return imageUrls.stream()
                .map(ImageLookupRequestDTO::new)
                .collect(Collectors.toList());

    }

    public static List<HashTagRequestDTO> extractHashTagRequestDTOs(List<StyleBoardRequestDTO.HashTagDto> hashTags) {

        return IntStream.range(0, hashTags.size())
                .mapToObj(i -> {
                    StyleBoardRequestDTO.HashTagDto hashTagDto = hashTags.get(i);
                    return new HashTagRequestDTO(
                            hashTagDto.getName(),
                            ContentType.STYLE_BOARD,
                            i // 리스트의 인덱스를 우선순위로 사용
                    );
                })
                .collect(Collectors.toList());
    }

    public static StyleBoardCreateDTO toStyleBoardCreateDTO(StyleBoardRequestDTO styleBoardRequestDTO) {

        return new StyleBoardCreateDTO(
                styleBoardRequestDTO.getCategory(),
                styleBoardRequestDTO.getTitle(),
                styleBoardRequestDTO.getContent()
                );
    }
}
