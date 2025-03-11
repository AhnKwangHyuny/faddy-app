package faddy.chat.repository;

import faddy.chat.domain.ChatRoom;
import faddy.chat.domain.ChatRoomUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface ChatRoomUserJpaRepository extends JpaRepository<ChatRoomUser, Long> {
    List<ChatRoomUser> findByUser_Id(Long userId);
    List<ChatRoomUser> findByChatRoom_Id(Long chatRoomId);

    @Query("delete from ChatRoomUser cu where cu.user.id = :userId and cu.chatRoom.id = :chatRoomId")
    void deleteByUserIdAndChatRoomId(Long userId, Long chatRoomId);

    @Query("delete from ChatRoomUser cu where cu.chatRoom.id = :roomId")
    void deleteChatRoomUsersByChatRoomId(Long roomId);

    Optional<ChatRoomUser> findByUserIdAndChatRoomId(Long userId, Long chatRoomId);

    // room에 user가 존재하는지 확인
    @Query("select count(cu) > 0 from ChatRoomUser cu where cu.user.id = :userId and cu.chatRoom.id = :chatRoomId")
    boolean existsByUserAndChatRoom(@Param("userId") Long userId, @Param("chatRoomId") Long chatRoomId);

    // room에 참여한 유저 수 카운팅
    @Query("select count(cu) from ChatRoomUser cu where cu.chatRoom.id = :chatRoomId")
    int countByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    // 해당 유저가 존재하는 모든 chatRoom id 조회
    @Query("SELECT cru.chatRoom.id FROM ChatRoomUser cru WHERE cru.user.id = :userId")
    Page<Long> findChatRoomIdsByUserId(@Param("userId") Long userId, Pageable pageable);

    // 해당 채팅방에 참여한 모든 유저 id 조회
    @Query("SELECT cru.user.id FROM ChatRoomUser cru WHERE cru.chatRoom.id = :chatRoomId")
    List<Long> findUserIdsByChatRoomId(@Param("chatRoomId") Long chatRoomId);

}