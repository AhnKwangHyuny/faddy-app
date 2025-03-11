package faddy.chat.presentation;

import faddy.auth.jwt.Service.JwtUtil;
import faddy.chat.domain.ChatRoom;
import faddy.chat.dto.request.CreateChatRoomRequest;
import faddy.chat.dto.request.UserChatRoomRequest;
import faddy.chat.dto.response.ChatRoomResponse;
import faddy.chat.dto.response.CreateChatRoomResponse;
import faddy.chat.service.ChatRoomValidationService;
import faddy.chat.service.LoadChatRoomService;
import faddy.chat.service.adapter.ChatRoomCreatePersistenceAdapter;
import faddy.chat.service.chatRoomService.ChatRoomService;
import faddy.chat.service.chatRoomUserService.ChatRoomUserService;
import faddy.global.response.ApiResponse;
import faddy.global.response.ErrorApiResponse;
import faddy.global.response.SuccessApiResponse;
import faddy.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/rooms")
public class ChatRoomController {

    private final ChatRoomCreatePersistenceAdapter chatRoomCreatePersistenceAdapter;
    private final LoadChatRoomService loadChatRoomService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomUserService chatRoomUserService;
    private final ChatRoomValidationService chatRoomValidationService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    @GetMapping
    public ResponseEntity<? extends ApiResponse> getRoomList(@RequestParam(value = "type", required = false) String type,
                                                             @RequestParam(name = "page", defaultValue = "0") int page,
                                                             HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);

        try {
            List<ChatRoomResponse> rooms;

            if (token == null || token.isEmpty()) {
                // 토큰이 없는 경우 전체 채팅방 목록 로드
                rooms = loadChatRoomService.getChatRooms(page);
            } else {
                // 토큰이 있는 경우 사용자 정보를 추출하여 사용자가 속한 채팅방 목록 로드
                Long userId = userService.getUserIdByAuthorization(token);
                rooms = loadChatRoomService.getUserChatRooms(page, userId);
            }

            return SuccessApiResponse.of(rooms);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ErrorApiResponse.of(HttpStatus.BAD_REQUEST, "채팅방을 불러오는데 실패했습니다.");
        }
    }


    @PostMapping
    public ResponseEntity<? extends ApiResponse> createRoom(@RequestBody CreateChatRoomRequest request){
        try {
            CreateChatRoomResponse newRoom = chatRoomCreatePersistenceAdapter.createChatRoom(request);
            return SuccessApiResponse.of(newRoom);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ErrorApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "채팅방 생성 실패");
        }
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<? extends ApiResponse> joinRoom(@RequestBody UserChatRoomRequest request,
                                                          @PathVariable Long roomId) {
        try {
            List<Long> userIds = userService.decryptUserIds(request.userIds());

            chatRoomUserService.addUserToChatRoom(loadChatRoomService.getChatRoomById(roomId),userIds);
            return SuccessApiResponse.of();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ErrorApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "채팅방 참여 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/{roomId}/invite")
    public ResponseEntity<? extends ApiResponse> inviteUserToChatRoom(@RequestBody UserChatRoomRequest request,
                                                                      @PathVariable Long roomId) {
        try {
            ChatRoom room = loadChatRoomService.getChatRoomById(roomId);

            List<Long> userIds = userService.decryptUserIds(request.userIds());

            chatRoomUserService.addUserToChatRoom(room , userIds);

            return SuccessApiResponse.of();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ErrorApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "채팅방 초대하는데 실패했습니다.");
        }
    }

    @PostMapping("/{roomId}/leave")
    public ResponseEntity<? extends ApiResponse> leaveRoom(@RequestBody UserChatRoomRequest request,
                                                           @PathVariable Long roomId) {
        try {
            chatRoomUserService.removeUserFromChatRoom(request.userIds().get(0) , roomId);
            return SuccessApiResponse.of();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ErrorApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "채팅방 나가기 중 오류가 발생했습니다.");
        }
    }

    // 채팅방 접근하는 user 접근 권한 검증
    @GetMapping("/{roomId}/validate-user")
    public ResponseEntity<? extends ApiResponse> validateUserInChatRoom(HttpServletRequest request ,
                                                                        @PathVariable("roomId") Long roomId) {
        try {
            String token = jwtUtil.getTokenFromRequest(request);
            Long userId = userService.getUserIdByAuthorization(token);

            boolean result = chatRoomValidationService.isUserInChatRoom(userId, roomId);
            if(result) {
                return SuccessApiResponse.of();
            } else {
                return ErrorApiResponse.of(HttpStatus.UNAUTHORIZED, "해당 채팅방에 접근할 수 없습니다.");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ErrorApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "사용자가 채팅방에 있는지 확인하는데 실패했습니다.");
        }
    }
}
