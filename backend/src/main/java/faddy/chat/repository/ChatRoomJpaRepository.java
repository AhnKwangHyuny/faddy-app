package faddy.chat.repository;

import faddy.chat.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom , Long> {
    @Query("SELECT cr FROM ChatRoom cr ORDER BY cr.created_at DESC")
    Page<ChatRoom> findAllTalksByPage(Pageable pageable);

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.id IN :ids ORDER BY cr.created_at DESC")
    List<ChatRoom> findChatRoomsByIds(@Param("ids") List<Long> ids);

}
