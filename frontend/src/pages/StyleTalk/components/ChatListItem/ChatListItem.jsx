import { Link, useNavigate } from 'react-router-dom';

const ChatListItem = ({ chatItem }) => {
    const navigate = useNavigate();

    const {
        roomId,
        title,
        createdAt,
        chatContentDto,
        roomMemberCount,
        thumbnailImage,
        type
    } = chatItem;

    // 마지막 채팅 내용을 형식화하는 헬퍼 함수
    const formatLastChatContent = (content, type) => {
        if (!content || !type) {
            return '메시지가 없습니다.';
        }

        switch (type) {
            case 'TEXT':
                return content;
            case 'IMAGE':
                return '사진을 보냈습니다.';
            case 'VIDEO':
                return '동영상을 보냈습니다.';
            case 'GIF':
                return 'GIF를 보냈습니다.';
            default:
                return '알 수 없는 메시지 유형입니다.';
        }
    };

    return (
        <div className="chatListItem">
            <Link to={`/talks/detail/${type?.toLowerCase()}/${roomId}`}>
                <div className="chatListItem-container">
                    <div className="chat-thumbnail">
                        <div className="chat-thumbnail-container">
                            <img
                                src={thumbnailImage || "/default_profile.jpg"}
                                alt="채팅 썸네일"
                            />
                        </div>
                    </div>

                    <div className="chat-label-container">
                        <div className="chat-label__title">
                            <span className="title">{title}</span>
                            <span className="headCount"> {roomMemberCount} </span>
                        </div>

                        <div className="chat-label__recentMessage">
                            {formatLastChatContent(chatContentDto?.content, chatContentDto?.type)}
                        </div>
                        <div className="chat-label__hashTag">
                            {/* <div className="hashTagList"> */}
                            {/*     <div className="hashTag"> */}
                            {/*         #테스트 */}
                            {/*     </div> */}
                            {/*     <div className="hashTag"> */}
                            {/*         #봄날씨 */}
                            {/*     </div> */}
                            {/*     <div className="hashTag"> */}
                            {/*         #추억 */}
                            {/*     </div> */}
                            {/* </div> */}
                        </div>
                    </div>

                    <div className="chat-metaInfo">
                        <div className="chat-metaInfo__unreadCount">
                            <div className="unreadCount-icon">
                                <span className="material-icons circle">
                                    circle
                                </span>
                                <div className="unreadCount">
                                    <span className="count">
                                        3
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div className="chat-metaInfo__recent-time">
                            <span className="time">
                                {createdAt}
                            </span>
                        </div>
                    </div>
                </div>
            </Link>
        </div>
    );
};

export default ChatListItem;
