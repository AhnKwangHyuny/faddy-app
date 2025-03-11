import { useParams, useSearchParams, useNavigate } from 'react-router-dom';
import CommentCard from "pages/styleBoardDetailPage/Components/CommentCard";
import getLevelData from "../../../utils/user/getLevelData";
import { testData, comments } from "pages/styleBoardDetailPage/data/testData";
import InputBox from "pages/styleBoardDetailPage/Components/InputBox";
import React, { useState, useEffect, memo } from 'react';

const CommentSection = memo(({ comments, onAddComment }) => {
    const [replyingTo, setReplyingTo] = useState(null);

    const handleReplyClick = (commentId) => {
        setReplyingTo(commentId);
    };


    useEffect(() => {
        console.log(replyingTo);
    }, [replyingTo]);

    const handleCancelReply = () => {
        setReplyingTo(null);
    };

    return (
        <div className="comment-section">
            <div className="comment-section__wrapper">
                <div className="comment-section__header">
                    <div className="comment-label">
                        <span className="label">
                            댓글 {comments.length}
                        </span>
                    </div>
                </div>
                <div className="content">
                    <ul className="comment-list">
                        {comments.map((comment) => (
                            <li className="comment-wrapper" key={comment.id}>
                                <CommentCard
                                    comment={comment}
                                    onReplyClick={() => handleReplyClick(comment.id)
                                    }
                                />
                            </li>
                        ))}
                    </ul>
                </div>
                {
                    replyingTo !== null && typeof replyingTo === 'number'
                        ? <InputBox
                            parentCommentId={replyingTo}
                            onAddComment={onAddComment}
                            placeholder={`@${comments.find((c) => c.id === replyingTo)?.author.nickname} 님에게 답글을 남겨주세요.`}
                          />
                        : <InputBox
                            onAddComment={onAddComment}
                            placeholder="자유롭게 댓글을 달아주세요."
                          />
                }
            </div>
        </div>
    );
});

export default CommentSection;
