import {faCommentDots, faHeart as fasHeart} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {faHeart} from "@fortawesome/free-regular-svg-icons";
import React from "react";



const CommentButton = () => {

    return (
        <div className="comment">
            <div className= "comment-button">
                <FontAwesomeIcon icon={faCommentDots}
                    className={`comment-icon`}
                />
            </div>

            <div className="comment-count">
                <span className="count">
                    30
                </span>
            </div>
        </div>
    );
}

export default CommentButton;