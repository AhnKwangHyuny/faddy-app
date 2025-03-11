import React, { useState } from 'react';
import getLevelData from "../../../utils/user/getLevelData";

const CommentCard = ({ comment, onReplyClick, onReplySubmit, isReplyOpen }) => {
    const [replyContent, setReplyContent] = useState('');

    const {color} = getLevelData(comment.user.level);
    const levelColor = color ? `#${color}` : '#000000';
    const handleReplyChange = (e) => {
        setReplyContent(e.target.value);
    };

    const handleSubmitReply = (e) => {
        e.preventDefault();
        onReplySubmit(replyContent);
        setReplyContent('');
    };

    return (
        <div className="comment-card">
            <div className="comment-header">
                <img className="avatar" src={comment.user.imageUrl} alt={comment.username} />
                <div className="profile-info">
                    <span className="level" style={{ color: levelColor }}>{comment.user.level}</span>
                    <span className="username">{comment.user.username}</span>
                    <span className="timestamp"> · {comment.createdAt}</span>
                </div>
            </div>

            <p className="comment-content">{comment.content}</p>

            {comment.replyComments && comment.replyComments.length > 0 && (
                <ul className="reply-list">
                    {comment.replyComments.map((replyComment) => (
                        <li className="reply-card" key={replyComment.id}>
                            <CommentCard replyComment={replyComment} />
                        </li>
                    ))}
                </ul>
            )}

            <div className="comment-meta">
                <div className="reply-button__wrapper">
                    <button className="reply-button" onClick={onReplyClick}>
                        <span>
                            답글
                        </span>
                    </button>

                    <div className="reply-count">
                        <span className="count">
                            2
                        </span>
                    </div>
                </div>

                <div className="like-button">
                    <div className="like-button__wrapper">
                        <span className="material-icons like">
                            thumb_up_off_alt
                        </span>
                    </div>

                    <div className="like-count">
                        <span className="count">
                            10
                        </span>
                    </div>
                </div>

            </div>

            {isReplyOpen && (
                <div className="reply-input-wrapper">
                    <input
                        className="reply-input"
                        type="text"
                        placeholder="답글을 작성하세요..."
                        value={replyContent}
                        onChange={handleReplyChange}
                    />
                    <button className="reply-submit-button" onClick={handleSubmitReply} disabled={!replyContent}>
                        등록
                    </button>
                </div>
            )}
        </div>
    );
};

export default CommentCard;