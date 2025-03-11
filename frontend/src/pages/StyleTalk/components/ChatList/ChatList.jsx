import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import ChatListItem from 'pages/StyleTalk/components/ChatListItem/ChatListItem';
import { getTalkList } from 'api/get';
import LoadingAnimation from 'shared/ui/LoadingUI/Loading';
import { useInView } from 'react-intersection-observer';

const ChatList = ({ chatType }) => {
    const [chatList, setChatList] = useState([]);
    const [loading, setLoading] = useState(false);
    const [ref, inView] = useInView();
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);

    const loadChatListByPaging = async (page, chatType) => {
        setLoading(true);
        try {
            const data = await getTalkList(chatType.toLowerCase(), page);
            console.log(data);
            setChatList(prevChatList => [...prevChatList, ...data]);
            setHasMore(data.length === 6); // 데이터가 6개일 때만 더 가져옴
        } catch (error) {
            console.error(`채팅 방 로딩 중 에러가 발생했습니다: ${error.message}`);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const fetchInitialChatList = async () => {
            setLoading(true);
            try {
                const response = await getTalkList(chatType.toLowerCase(), 0);
                setChatList(response);
                setHasMore(response.length === 6);
                setPage(1);
            } catch (error) {
                console.error("Failed to fetch chat list", error);
            } finally {
                setLoading(false);
            }
        };
        fetchInitialChatList();
    }, [chatType]);

    useEffect(() => {
        if (inView && hasMore && !loading) {
            loadChatListByPaging(page, chatType).then(() => {
                setPage(prevPage => prevPage + 1);
            });
        }
    }, [inView, hasMore, loading, chatType, page]);

    return (
        <section id="chatList">
            {chatList.map((chatItem, index) => (
                <ChatListItem key={index} chatItem={chatItem} />
            ))}
            {loading && <LoadingAnimation />}
            <div ref={ref} />
        </section>
    );
};

ChatList.propTypes = {
    chatType: PropTypes.string.isRequired
};

export default ChatList;