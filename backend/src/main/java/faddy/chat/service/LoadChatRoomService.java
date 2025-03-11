package faddy.chat.service;

import faddy.chat.domain.ChatRoom;
import faddy.chat.dto.response.ChatRoomResponse;
import faddy.chat.repository.ChatRoomJpaRepository;
import faddy.chat.repository.ChatRoomUserJpaRepository;
import faddy.chat.service.chatRoomService.ChatRoomService;
import faddy.chat.service.useCase.ChatRoomLoadUseCase;
import faddy.global.exception.BadRequestException;
import faddy.global.exception.ExceptionCode;
import faddy.global.response.SuccessApiResponse;
import faddy.snap.domain.Snap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class LoadChatRoomService implements ChatRoomLoadUseCase {

    private final ChatRoomJpaRepository chatRoomRepository;
    private final ChatRoomUserJpaRepository chatRoomUserRepository;
    private final ChatRoomService chatRoomService;

    @Override
    public ChatRoom getChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.CHATROOM_NOT_FOUND_ERROR));
    }

    @Override
    public List<ChatRoomResponse> getChatRooms(int page) {

        // db에 snap 데이터 요청 per page
        Pageable pageable = PageRequest.of(page, 6);

        List<ChatRoom> chatRooms = chatRoomRepository.findAllTalksByPage(pageable).getContent();
        
        for (ChatRoom chatRoom : chatRooms) {
            System.out.println("chatRoom.toString() = " + chatRoom.toString());
        }
        
        return chatRooms.stream()
                .map(chatRoomService::mapToChatRoomResponse)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getUserChatRooms(int page, Long userId) {

        try {
            Pageable pageable = PageRequest.of(page-1, 6);
            System.out.println("page = " + page);
            List<Long> chatRoomIds = chatRoomUserRepository.findChatRoomIdsByUserId(userId, pageable).getContent();

            // 채팅 리스트가 업는 경우 빈 리스트 반영
            if (chatRoomIds.isEmpty()) {
                log.warn("No chat rooms found for userId: {}", userId);
                return Collections.emptyList();
            }

            return chatRoomRepository.findChatRoomsByIds(chatRoomIds).stream()
                    .map(chatRoomService::mapToChatRoomResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException(ExceptionCode.CHATROOM_NOT_FOUND_ERROR);
        }

    }
}

