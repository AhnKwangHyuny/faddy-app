package faddy.chat.service.chatRoomUserService;

import faddy.chat.domain.ChatRoom;

import java.util.List;

public interface ChatRoomUserService {

    void addUserToChatRoom(ChatRoom room, List<Long> memberIds);
    void removeUserFromChatRoom(String userId, Long roomId);
    void inviteUserToChatRoom(List<String> users, Long roomId);

    /**
     *  해당 채팅방에 존재하는 모든 유저 id 조화
     *  @param chatRoomId 채팅방 id
     *  @return 채팅방에 존재하는 모든 유저 id
     * */
    List<Long> getUserIdsByChatRoomId(Long chatRoomId);

}
