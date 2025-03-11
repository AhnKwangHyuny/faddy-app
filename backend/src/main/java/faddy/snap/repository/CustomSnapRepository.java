package faddy.snap.repository;

import faddy.snap.domain.Snap;
import faddy.snap.domain.dto.response.SnapResponseDto;

import java.util.Optional;

public interface CustomSnapRepository {
    Optional<SnapResponseDto> findSnapWithSnapDto(Long snapId);

    Optional<Snap> findSnapById(Long snapId);
}
