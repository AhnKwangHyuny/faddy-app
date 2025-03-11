import React from 'react';
import PropTypes from 'prop-types';
import { isValidURL, isImageURL } from "utils/utils";

const MessageContent = ({ message, type }) => {
    const renderContent = () => {
        switch(type) {
            case 'image':
                if (isImageURL(message)) {
                    return <img src={message} alt="이미지" className="message-content-image" />;
                }
                return <img src={message} alt="이미지" className="message-content-image" />;
            case 'url':
                if (isValidURL(message)) {
                    return (
                        <a href={message} target="_blank" rel="noopener noreferrer" className="message-content-url">
                            {message}
                        </a>
                    );
                }
                return <span>Invalid URL</span>;
            case 'text':
            case 'error':
            case 'etc':
            case 'system':
            case 'timestamp':
                if (isValidURL(message)) {
                    return (
                        <a href={message} target="_blank" rel="noopener noreferrer" className="message-content-url">
                            {message}
                        </a>
                    );
                } else if (isImageURL(message)) {
                    return <img src={message} alt="이미지" className="message-content-image" />;
                }
                return <div className="message-container-text">{message}</div>;
            default:
                return <div className="message-container-text" >Unsupported message type</div>;
        }
    };

    return (
        <div className="message-content">
            {renderContent()}
        </div>
    );
};

MessageContent.propTypes = {
    message: PropTypes.string.isRequired,
    type: PropTypes.oneOf(['image', 'url', 'text', 'error', 'etc', 'system', 'timestamp']).isRequired,
};

export default MessageContent;
