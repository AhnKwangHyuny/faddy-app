import SockJS from "sockjs-client";
import React, { useState, useEffect, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Stomp } from "@stomp/stompjs";
import { fetchChatMessages,
        checkChatRoomAccess,
        fetchChatRoomUserProfile ,
        fetchChatRoomUserProfiles
        } from "api/get";

import { isValidURL } from "utils/utils";
import ContentBox from "features/ContentBox/ContentBox";
import Avatar from "widgets/Avatar/Avatar";
import AboutHeader from "features/AboutHeader/AboutHeader";
import MainContainer from "pages/StyleTalkRoom/components/MainContainer";
import ChatContainer from 'pages/StyleTalkRoom/components/ChatContainer';
import MessageList from 'pages/StyleTalkRoom/components/MessageList';
import MessageGroup from 'pages/StyleTalkRoom/components/MessageGroup';
import MessageMetaInfo from 'pages/StyleTalkRoom/components/MessageMetaInfo';
import Message from 'pages/StyleTalkRoom/components/Message';
import SentTime from 'pages/StyleTalkRoom/components/SentTime';
import ReadCount from 'pages/StyleTalkRoom/components/ReadCount';
import TimeStampLine from "pages/StyleTalkRoom/components/TimeStampLine";
import DividerLine from "pages/StyleTalkRoom/components/DividerLine";
import MessageContent from "pages/StyleTalkRoom/components/MessageContent";

const getCurrentUsername = () => {
    return localStorage.getItem("username");
};

/**
 * API response message data를 채팅방에 맞게 변환
 */
const convertResponseToChat = (messageData, userProfiles) => {
    const currentUsername = getCurrentUsername();
    let direction;

    const messageType = messageData.type.toLowerCase();

    if (messageType === "system" || messageType === "timestamp") {
        direction = "center";
    } else {
        direction = messageData.sender === currentUsername ? "incoming" : "outgoing";
    }
    return {
        model: {
            content: messageData.content,
            direction: direction,
            type: messageType,
            createdAt : messageData.createdAt
        },
        avatar: direction === "outgoing" ? {
            profileImageUrl: userProfiles[messageData.sender]?.profileImageUrl || "/default_profile.jpg",
            nickname: userProfiles[messageData.sender]?.nickname || "알 수 없음",
            level: userProfiles[messageData.sender]?.level || null
        } : null,
    };
};


/**
 * 채팅 메세지 컴포넌트 생성
 */
const getMessageComponent = (data) => (
    Array.isArray(data) ? data.map((item, index) => (
        <MessageGroup key={index}>
            {item.avatar && <Avatar userInfo={item.avatar} />}
            <Message model={item.model}>
                <MessageContent message={item.model.content} type={item.model.type} />
                <MessageMetaInfo>
                    <SentTime createdAt = {item.model.createdAt} />
                    <ReadCount />
                </MessageMetaInfo>
            </Message>
        </MessageGroup>
    )) : null
);


const ChatRoom = () => {
    const { type, id } = useParams();
    const navigate = useNavigate();
    const [messages, setMessages] = useState([]);
    const [inputValue, setInputValue] = useState("");
    const [stompClient, setStompClient] = useState(null);
    const [userProfiles, setUserProfiles] = useState({});
    const messageEndRef = useRef(null);

    const scrollToBottom = () => {
        messageEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    /**
     * 과거 chat 목록을 가져옴
     */
    const loadChats = async (userProfiles) => {
        try {
            const response = await fetchChatMessages(id);
            const chatMessages = response.map((message) => convertResponseToChat(message, userProfiles));
            setMessages(chatMessages);
        } catch (error) {
            console.error('채팅 리스트를 조회하는데 실패했습니다.', error);
        }
    };

    /**
     * 채팅방에 등록된 사용자인지 인증을 통해 채팅방 접근 권한을 확인
     */
    const checkUserPermission = async (roomId) => {
        try {
            const result = await checkChatRoomAccess(roomId);
            if (!result) {
                alert('채팅방 접근 권한이 없습니다.');
                navigate(-1);
            }
        } catch (error) {
            console.error('채팅방 접근 권한을 확인하는데 실패했습니다.', error);
            alert('채팅방 접근 권한을 확인하는데 실패했습니다.');
            navigate(-1);
        }
    };

    /**
    * 채팅방 입장 시 해당 유저 프로필 정보를 가져와 저장
    */
    const fetchAndStoreUserProfile = async (username) => {
        try {
            const userProfile = await fetchChatRoomUserProfile(username);

            setUserProfiles((prevProfiles) => ({
                ...prevProfiles,
                ...userProfile,
            }));


        } catch (error) {
            console.error('사용자 프로필을 가져오는데 실패했습니다.', error);
        }
    };

    /**
    * 초기 채팅방 입장 시 해당 유저 프로필 정보를 가져와 저장
    */
    const fetchAndStoreUserProfiles = async (roomId) => {

        try {
            const userProfiles = await fetchChatRoomUserProfiles(roomId);

            setUserProfiles(userProfiles);

            return userProfiles;
        } catch (error) {
            console.error('사용자 프로필을 가져오는데 실패했습니다.', error);
        }
    };


    /**
     * 채팅방 나갈 시 해당 유저 프로필 저장 정보 삭제
     */
     const deleteStoredUserProfile = (username) => {
        setUserProfiles((prevProfiles) => {
            const newProfiles = { ...prevProfiles };
            delete newProfiles[username];
            return newProfiles;
        })
    };


    /**
    * 채팅방 입장 시 유저 입장 알림 시스템 메세지 sub에 전달
    */
    const handleEnter = async (client, headers) => {
        const token = `${localStorage.getItem('GRANT_TYPE')} ${localStorage.getItem('ACCESS_TOKEN')}`;
        client.send(`/pub/talks/${id}/enter`, headers, JSON.stringify({ token }));
    };

    /**
    * 채팅방 입장시 socket 연결
    */
    useEffect(() => {
        const socket = new SockJS("/ws/chat");
        const client = Stomp.over(socket);
        setStompClient(client);

        const token = localStorage.getItem('ACCESS_TOKEN');
        const bearer = localStorage.getItem('GRANT_TYPE');

        if (!token || !bearer) {
            console.error('토큰 또는 권한 유형이 누락되었습니다.');
            alert('인증 정보가 누락되었습니다. 다시 로그인해 주세요.');
            navigate('/login');
            return;
        }

        const headers = {
            Authorization: `${bearer} ${token}`,
        };

        client.connect(headers, async (frame) => {

            // 채팅방 접근 권한 확인
            await checkUserPermission(id);

            // 채팅방 입장 시 유저 입장 알림 시스템 메세지 sub에 전달
            await handleEnter(client, headers);

            // 유저 프로필 정보 캐싱 후 저장
            const userProfiles = await fetchAndStoreUserProfiles(id);

            await loadChats(userProfiles); // 채팅방 메세지 로드 (프로필 캐싱)

            // 구독 요청
            client.subscribe(`/sub/talks/${id}`, (message) => {
                const response = JSON.parse(message.body);
                if (Array.isArray(response)) {

                    const newChats = response.map((chat) => {
                        return convertResponseToChat(chat, userProfiles)
                    });

                    setMessages((prevMessages) => [...prevMessages, ...newChats]);

                } else {
                    const newChat = convertResponseToChat(response, userProfiles);
                    setMessages((prevMessages) => [...prevMessages, newChat]);
                }
            });
        });

        return () => {
            if (client) {

                client.disconnect();
            }
        };
    }, [id]);

    // 채팅방 메세지 보내기
    const handleSend = (input) => {
        if (!input.trim()) return;

        const contentType = isValidURL(input) ? "IMAGE" : "TEXT";

        const token = `${localStorage.getItem('GRANT_TYPE')} ${localStorage.getItem('ACCESS_TOKEN')}`;

        const newMessage = {
            contentType: contentType,
            content: input,
            token,
        };

        if (stompClient) {
            stompClient.send(`/pub/talks/${id}/send`, {}, JSON.stringify(newMessage));
        }

        setInputValue("");
    };

    return (
        <section id="Style-talk">
            <AboutHeader title={"#title"} />
            <DividerLine />
            <MainContainer>
                <ChatContainer>
                    <MessageList>
                        {getMessageComponent(messages)}
                        <div ref={messageEndRef} />
                    </MessageList>
                </ChatContainer>
                <ContentBox
                    icon={<span className="material-icons button-icon">sentiment_satisfied_alt</span>}
                    onSubmit={handleSend}
                    value={inputValue}
                    onChange={(e) => setInputValue(e.target.value)}
                />
            </MainContainer>
        </section>
    );
};

export default ChatRoom;
