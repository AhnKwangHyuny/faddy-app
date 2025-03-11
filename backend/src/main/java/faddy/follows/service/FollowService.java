package faddy.follows.service;

import faddy.follows.domain.Follow;
import faddy.follows.domain.dto.response.FollowResponseDto;
import faddy.follows.repository.FollowJpaRepository;
import faddy.global.exception.ExceptionCode;
import faddy.global.exception.FollowException;
import faddy.user.domain.User;
import faddy.user.repository.UserRepository;
import faddy.user.service.UserIdEncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowJpaRepository followRepository;
    private final UserIdEncryptionUtil userIdEncryptionUtil;
    private final UserRepository userRepository;
    /**
     * follow 요청
     * @Param User 팔로우 요청 유저 , User 팔로우 받은 유저
     * @Return 요청 결과 message
     * */
    @Transactional
    public String follow(User following , User follower) {

        // 자기 자신 follow 안됨
        if (follower == following)
            throw new FollowException(ExceptionCode.FAIL_SELF_FOLLOW_ERROR);

        // 중복 follow x
        if (followRepository.existsByFollowingAndFollower(following , follower)){
            throw new FollowException(ExceptionCode.ALREADY_FOLLOW_ERROR);
        }

        // Follow 엔티티 생성
        Follow follow = Follow.builder()
                .following(following)
                .follower(follower)
                .build();

        // follower , followee follow List 업데이트
        follower.getFollowings().add(follow);
        following.getFollowers().add(follow);

        followRepository.save(follow);

        return "팔로우에 성공했습니다. (상대방에게 메세지 전송)";
    }

    /**
     * unfollow 요청
     * @Param User 언팔로우 요청 유저 , User 언팔로우 받은 유저
     * @Return void
     * */
    @Transactional
    public void cancelFollow(User following , User follower) {
        Optional<Follow> follow = followRepository.deleteByFollowingAndFollower(following, follower);

        if (!follow.isPresent()) {
            throw new FollowException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "시스템 오류로 인해 언팔로우에 실패했습니다."
            );
        }

        // 언팔 성공 시 follower , followee followList 갱신
        following.getFollowers().remove(follow.get());
        System.out.println("following = " + following.getFollowers());
        follower.getFollowings().remove(follow.get());


    }

    @Transactional(readOnly = true)
    public Boolean isFollowed(User following , User follower) {

        return followRepository.existsByFollowingAndFollower(following, follower);
    }

    /**
     *  사용자가 팔로우한 팔로워 목록 조회
     *  @Param String decrypted userId
     *  @Return List<FollowResponseDto>
     */
    @Transactional(readOnly = true)
    public List<FollowResponseDto> getFollowings(String userId) {

        Long decryptUserId = userIdEncryptionUtil.decryptUserId(userId);

        // if decryptUserId is null, throw exception
        if (decryptUserId == null) {
            throw new FollowException(ExceptionCode.DECRYPT_USER_ID_ERROR);
        }

        User user = userRepository.findById(decryptUserId).orElseThrow(() -> new IllegalArgumentException("존재하지 않은 유저입니다."));

        return followRepository.findAllByFollowerId(decryptUserId).stream()
                .map(follow -> new FollowResponseDto.Builder()
                        .userId(userIdEncryptionUtil.encryptUserId(follow.getFollower().getId()))
                        .nickname(follow.getFollower().getNickname())
                        .profileImageUrl(follow.getFollower().getProfile().getProfileImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     *  나를 팔로우한 사용자 목록 조회
     *  @Param String decrypted userId
     *  @Return List<FollowResponseDto>
     * */
    @Transactional(readOnly = true)
    public List<FollowResponseDto> getFollowers(String userId) {

        Long decryptUserId = userIdEncryptionUtil.decryptUserId(userId);

        // if decryptUserId is null, throw exception
        if (decryptUserId == null) {
            throw new FollowException(ExceptionCode.DECRYPT_USER_ID_ERROR);
        }

        User user = userRepository.findById(decryptUserId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        List<FollowResponseDto> a =  followRepository.findAllByFollowingId(decryptUserId).stream()
                .map(follow -> new FollowResponseDto.Builder()
                        .userId(userIdEncryptionUtil.encryptUserId(follow.getFollower().getId()))
                        .nickname(follow.getFollower().getNickname())
                        .profileImageUrl(follow.getFollower().getProfile().getProfileImageUrl())
                        .build())
                .collect(Collectors.toList());

        for (int c = 0 ; c < a.size() ; c++) {
            System.out.println(a.get(c).getNickname());
        }

        return a;
    }

}
