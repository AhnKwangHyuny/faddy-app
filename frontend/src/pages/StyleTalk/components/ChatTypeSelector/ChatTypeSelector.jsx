import React from 'react';

const ChatTypeSelector = ({ onChatTypeChange }) => {
    // 오픈채팅을 클릭했을 때 호출될 함수
    const handleOpenChatClick = () => {
        onChatTypeChange('open');
    };

    // 친구채팅을 클릭했을 때 호출될 함수
    const handleFriendChatClick = () => {
        onChatTypeChange('friend');
    };

    return (
        <section id="ChatTypeSelector">
            <div className="chatTypeSelector__container">
                <div className="chat-name__container">
                    <div className="chat-name">
                        <div className="chat-name__wrapper">
                            <button
                                className="chat-name__button"
                                onClick={handleOpenChatClick}
                            >
                                <span className="sorting-name">오픈톡</span>
                            </button>
                        </div>
                    </div>
                    <div className="chat-name">
                        <div className="chat-name__wrapper">
                            <button
                                className="chat-name__button"
                                onClick={handleFriendChatClick}
                            >
                                <span className="sorting-name">친구톡</span>
                            </button>
                        </div>
                    </div>
                </div>
                <hr className="hdlne line" />
            </div>
        </section>
    );
};

export default ChatTypeSelector;
