//package faddy.snap.repository;
//
//import com.querydsl.core.types.Projections;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import faddy.hashTags.domain.QHashTag;
//import faddy.image.domain.QImage;
//import faddy.snap.domain.QSnap;
//import faddy.snap.domain.Snap;
//import faddy.snap.domain.dto.response.SnapResponseDto;
//import faddy.user.domain.QProfile;
//import faddy.user.domain.QUser;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Repository
//@RequiredArgsConstructor
//public class CustomSnapRepositoryImpl implements CustomSnapRepository {
//
//    private final JPAQueryFactory queryFactory;
//
//    @Override
//    public Optional<SnapResponseDto> findSnapWithSnapDto(Long snapId) {
//        QSnap snap = QSnap.snap;
//        QUser user = QUser.user;
//        QImage image = QImage.image;
//        QHashTag hashTag = QHashTag.hashTag;
//        QProfile profile = QProfile.profile;
//
//        SnapResponseDto snapResponseDto = queryFactory
//                .select(Projections.constructor(SnapResponseDto.class,
//                        Projections.constructor(SnapResponseDto.UserDto.class,
//                                user.id,
//                                user.username,
//                                user.nickname,
//                                user.email,
//                                Projections.constructor(SnapResponseDto.ProfileDto.class,
//                                        profile.id,
//                                        profile.motto,
//                                        profile.userLevel)
//                        ),
//                        Projections.constructor(SnapResponseDto.ImageDto.class,
//                                image.imageUrl,
//                                image.hashName
//                        ),
//                        Projections.constructor(SnapResponseDto.HashTagDto.class,
//                                hashTag.name
//                        ),
//                        snap.description,
//                        snap.created_at
//                ))
//                .from(snap)
//                .fetchJoin()
//                .leftJoin(user).on(snap.user.eq(user))
//                .fetchJoin()
//                .leftJoin(profile).on(user.profile.eq(profile))
//                .fetchJoin()
//                .leftJoin(image).on(snap.snapImages.contains(image))
//                .fetchJoin()
//                .leftJoin(hashTag).on(snap.hashTags.contains(hashTag))
//                .where(snap.id.eq(snapId).and(snap.deletedAt.isNull()))
//                .fetchOne();
//
//
//        return Optional.ofNullable(snapResponseDto);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Optional<Snap> findSnapById(Long snapId) {
//        QSnap snap = QSnap.snap;
//        QUser user = QUser.user;
//        QImage image = QImage.image;
//        QHashTag hashTag = QHashTag.hashTag;
//
//        Snap result = queryFactory
//                .selectFrom(snap)
//                .leftJoin(snap.user, user).fetchJoin()
//                .leftJoin(snap.snapImages, image).fetchJoin()
//                .leftJoin(snap.hashTags, hashTag).fetchJoin()
//                .where(snap.id.eq(snapId).and(snap.deletedAt.isNull()))
//                .fetchOne();
//
//        return Optional.ofNullable(result);
//    }
//}