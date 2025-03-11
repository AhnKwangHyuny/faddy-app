import React, { useState } from 'react';
import { getLevelInfo } from "data/LevelData";
import { mapToComment } from "pages/styleBoardDetailPage/utils/responseMapper";
import { createCommentLike } from "pages/styleBoardDetailPage/api/post";
import { cancelLike } from "pages/styleBoardDetailPage/api/delete";
import {mapContentTypeToLabel} from "data/contentType";


const CommentCard = ({ comment, onReplyClick }) => {
    const adComment = mapToComment(comment);
    const levelInfo = getLevelInfo(adComment.author.level);
    const [isLiked, setIsLiked] = useState(adComment.isLiked);
    const [likeCount, setLikeCount] = useState(adComment.likeCount);
    const contentType = mapContentTypeToLabel("styleBoardComment");

    /**
      like 버튼 클릭 시 좋아요 추가/취소 핸들링
     */
    const handleLikeClick = async () => {

        try {
            if (isLiked) {
                await cancelLike(contentType, adComment.id);
            } else {
                await createCommentLike(contentType, adComment.id);
            }

            setIsLiked(!isLiked);
            setLikeCount(isLiked ? likeCount - 1 : likeCount + 1);

        } catch (error) {
            console.error('Error updating like:', error);
            setIsLiked(isLiked); // 원래 상태로 되돌림
            setLikeCount(isLiked ? likeCount + 1 : likeCount - 1); // 원래 카운트로 되돌림
        }
    };

    return (
        <div className="comment-card">
            <div className="comment-card__container">
                <div className="avatar-container">
                    <img className="avatar" src={adComment.author.profileImageUrl} alt={adComment.author.nickname} />
                </div>
                <div className="comment-details">
                    <div className="profile-info">
                        <span className="level" style={{ color: levelInfo.color }}>
                            {levelInfo.level} {levelInfo.name}
                        </span>
                        <span className="username">{adComment.author.nickname}</span>
                        <span className="timestamp"> · {adComment.created_at}</span>
                    </div>
                    <p className="comment-content">{adComment.content}</p>
                    <div className="comment-meta">
                        <div className="reply-button" onClick={onReplyClick}>
                            <span>답글</span>
                        </div>
                        <div className="like-button" onClick={handleLikeClick}>
                            <span className={`material-icons like ${isLiked ? 'liked' : ''}`}>
                                thumb_up_off_alt
                            </span>
                            {likeCount > 0 && (
                                <span className="count">{likeCount}</span>
                            )}
                        </div>
                    </div>
                </div>
                <div className="more-button">
                    <span className="material-icons more">more_vert</span>
                </div>
            </div>

            {adComment.replies && adComment.replies.length > 0 && (
                <ul className="reply-list">
                    {adComment.replies.map((reply) => (
                        <li className="reply-card-item" key={reply.id}>
                            <ReplyCard reply={reply} />
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

const ReplyCard = ({ reply }) => {
    const adReply = mapToComment(reply);
    const levelInfo = getLevelInfo(adReply.author.level);
    const [isLiked, setIsLiked] = useState(adReply.isLiked);
    const [likeCount, setLikeCount] = useState(adReply.likeCount);
    const contentType = mapContentTypeToLabel("styleBoardComment");


    const handleLikeClick = async () => {

        try {
            if (isLiked) {
                await cancelLike(contentType, adReply.id);
            } else {
                await createCommentLike(contentType, adReply.id);
            }

            setIsLiked(!isLiked);
            setLikeCount(isLiked ? likeCount - 1 : likeCount + 1);

        } catch (error) {
            console.error('Error updating like:', error);
            setIsLiked(isLiked); // 원래 상태로 되돌림
            setLikeCount(isLiked ? likeCount + 1 : likeCount - 1); // 원래 카운트로 되돌림
        }
    };

    return (
        <div className="reply-card">
            <div className="reply-card__container">
                <div className="avatar-container">
                    <img className="avatar" src={adReply.author.profileImageUrl} alt={adReply.nickname} />
                </div>
                <div className="comment-details">
                    <div className="profile-info">
                        <span className="level" style={{ color: levelInfo.color }}>
                            {levelInfo.level} {levelInfo.name}
                        </span>
                        <span className="username">{adReply.author.nickname}</span>
                        <span className="timestamp"> · {adReply.created_at}</span>
                    </div>
                    <p className="comment-content">{adReply.content}</p>
                    <div className="reply-meta">
                        <div className="like-button-reply" onClick={handleLikeClick}>
                            <span className={`material-icons like-rp ${isLiked ? 'liked' : ''}`}>
                                thumb_up_off_alt
                            </span>
                            {likeCount > 0 && (
                                <span className="count-rp">{likeCount}</span>
                            )}
                        </div>
                    </div>
                </div>
                <div className="more-button">
                    <span className="material-icons more">more_vert</span>
                </div>
            </div>
        </div>
    );
};

export default CommentCard;
