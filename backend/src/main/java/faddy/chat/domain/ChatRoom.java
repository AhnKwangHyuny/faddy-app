package faddy.chat.domain;

import faddy.chat.type.ChatRoomType;
import faddy.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name ="chat_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    private Long masterId;

    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ChatRoomType type;

    // 마지막 채팅 시간 저장
    private LocalDateTime lastChatTime;

    // category , hashTag 추후 업데이트


    @Builder
    public ChatRoom(Long masterId, ChatRoomType type) {
        this.masterId = masterId;
        this.type = type;
        this.title = null;
    }

    public void updateChatRoom(Long masterId, String title) {
        this.masterId = masterId;
        this.title = title;
    }

    // create builder using type and masterId except title
    public static ChatRoom createChatRoom(Long masterId, ChatRoomType type) {
        return ChatRoom.builder()
                .masterId(masterId)
                .type(type)
                .build();
    }

    public void updateChatRoom(String title) {
        this.title = title;
    }

    //마지막 chat 시간 업데이트
    public void updateLastChatTime(LocalDateTime lastChatTime) {
        this.lastChatTime = lastChatTime;
    }

    public void changeTitle(String title) {
        this.title = title;
    }
}
