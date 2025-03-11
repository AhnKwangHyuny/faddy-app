package faddy.snap.infrastructure.mapper;

import faddy.hashTags.domain.HashTag;
import faddy.image.domain.Image;
import faddy.snap.domain.Snap;
import faddy.snap.domain.dto.response.SnapResponseDto;
import faddy.user.domain.Profile;
import faddy.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SnapMapper {

    SnapResponseDto toDto(Snap snap);

    @Mapping(source = "user", target = ".")
    SnapResponseDto.UserDto toUserDto(User user);

    @Mapping(source = "profile", target = ".")
    SnapResponseDto.ProfileDto toProfileDto(Profile profile);

    List<SnapResponseDto.ImageDto> toImageDtos(List<Image> images);

    SnapResponseDto.ImageDto toImageDto(Image image);

    List<SnapResponseDto.HashTagDto> toHashTagDtos(List<HashTag> hashTags);

    SnapResponseDto.HashTagDto toHashTagDto(HashTag hashTag);

    default SnapResponseDto toDto(Snap snap, List<Image> images, List<HashTag> hashTags, User user) {
        SnapResponseDto dto = new SnapResponseDto();
        dto.setUser(toUserDto(user));
        dto.setImages(toImageDtos(images));
        dto.setHashTags(toHashTagDtos(hashTags));
        dto.setDescription(snap.getDescription());
        dto.setCreated_at(snap.getCreated_at());
        return dto;
    }
}