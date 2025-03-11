
import React, { useState } from 'react';
import LikeButton from "widgets/LikeButton/LikeButton";
import ShareButton from "widgets/ShareButton/ShareButton";
import CommentButton from "widgets/Comment/CommentButton/CommentButton";
import {postLike} from "api/post";
import {deleteLike} from "api/delete";

const Interaction = (objectId) => {
    const [isLiked, setIsLiked] = useState(false);
    const [commentOpen, setCommentOpen] = useState(false);

    const handleLikeClick = () => {

        if (isLiked) {
            deleteLike(objectId.objectId);
        } else {
            postLike(objectId.objectId);
        }
        setIsLiked(!isLiked);
    };


    const handleCommentClick = () => {
        setCommentOpen(!commentOpen);
    };

    const handleShareClick = () => {
        // 공유 기능 구현
    };

    return (
        <section className="interaction">
            <LikeButton onLikeChange={handleLikeClick}/>
            <CommentButton/>
            <ShareButton/>
        </section>
    );
};

export default Interaction;