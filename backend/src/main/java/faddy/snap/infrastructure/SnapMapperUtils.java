package faddy.snap.infrastructure;

import faddy.hashTags.domain.projection.HashTagProjection;
import faddy.image.domain.projection.ImageProjection;
import faddy.profile.domain.projection.ProfileProjection;
import faddy.snap.domain.dto.response.SnapResponseDto;
import faddy.user.domain.projection.UserProjection;

public class SnapMapperUtils {
//    public static SnapResponseDto mapToDto(SnapProjection snapProjection) {
//        return SnapResponseDto(
//                .user(mapToUserDto(snapProjection.getUser()))
//                .images(snapProjection.getImages().stream()
//                        .map(SnapMapper::mapToImageDto)
//                        .collect(Collectors.toList()))
//                .hashTags(snapProjection.getHashTags().stream()
//                        .map(SnapMapper::mapToHashTagDto)
//                        .collect(Collectors.toList()))
//                .description(snapProjection.getDescription())
//                .createdAt(snapProjection.getCreated_at())
//                .build();
//            )
//    }



    private static SnapResponseDto.UserDto mapToUserDto(UserProjection userProjection) {
        return SnapResponseDto.UserDto.builder()
                .id(userProjection.getId())
                .username(userProjection.getUsername())
                .nickname(userProjection.getNickname())
                .email(userProjection.getEmail())
                .build();
    }

    private static SnapResponseDto.ProfileDto mapToProfileDto(ProfileProjection profileProjection) {
        return SnapResponseDto.ProfileDto.builder()
                .id(profileProjection.getId())
                .motto(profileProjection.getMotto())
                .userLevel(profileProjection.getUserLevel())
                .build();
    }

    private static SnapResponseDto.ImageDto mapToImageDto(ImageProjection imageProjection) {
        return SnapResponseDto.ImageDto.builder()
                .imageUrl(imageProjection.getImageUrl())
                .hashName(imageProjection.getHashedName())
                .build();
    }

    private static SnapResponseDto.HashTagDto mapToHashTagDto(HashTagProjection hashTagProjection) {
        return SnapResponseDto.HashTagDto.builder()
                .name(hashTagProjection.getName())
                .build();
    }
}