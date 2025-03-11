import React, { useState } from 'react';
import { useParams } from 'react-router-dom';

const InputBox = ({ parentCommentId = null, onAddComment, placeholder }) => {
    const { id } = useParams();
    const [comment, setComment] = useState('');

    const handleCommentChange = (e) => {
        setComment(e.target.value);
    };

    const handleSubmit = () => {
        onAddComment(comment, parentCommentId);
        setComment('');
    };

    return (
        <div className="input-box">
            <div className="input-box__wrapper">
                <div className="emoticon-box">
                    <div className="emoticon-icon">
                        <span className="material-icons button-icon">
                            sentiment_satisfied_alt
                        </span>
                    </div>
                </div>
                <div className="input-field">
                    <input
                        type="text"
                        className="text-form"
                        placeholder={placeholder}
                        value={comment}
                        onChange={handleCommentChange}
                    />
                </div>
                <div className="submit-comment">
                    <button
                        className="submit-button"
                        onClick={handleSubmit}
                        disabled={!comment.trim()}
                    >
                        <span className="material-icons">send</span>
                    </button>
                </div>
            </div>
        </div>
    );
};

export default InputBox;
