package faddy.styleBoard.dto.request;

import lombok.*;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FilteredStyleBoardRequestDTO {
    private String sort;
    private String category;
    private int page;
    private int size;
    private List<String> tags;

}
