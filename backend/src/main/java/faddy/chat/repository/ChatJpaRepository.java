package faddy.chat.repository;

import faddy.chat.domain.Chat;
import faddy.chat.domain.ChatRoom;
import faddy.chat.dto.LastChatContentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatJpaRepository extends JpaRepository<Chat , Long> {

    List<Chat> findByChatRoom(ChatRoom chatRoom);


    /**
     * 해당 채팅방에서 가장 최근 메시지의 content와 type을 조회
     * @param chatRoom 대상 채팅방
     * @return 가장 최근 메시지의 content와 type
     */
    @Query("""
        SELECT NEW faddy.chat.dto.LastChatContentDto(c.content, c.type) 
        FROM Chat c 
        WHERE c.chatRoom = :chatRoom 
        ORDER BY c.created_at DESC
        """)
    List<LastChatContentDto> findAllByChatRoomOrderByCreatedAtDesc(@Param("chatRoom") ChatRoom chatRoom);




}