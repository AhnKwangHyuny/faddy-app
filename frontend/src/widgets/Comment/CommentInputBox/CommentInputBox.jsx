import React, {useEffect, useState} from 'react';
import CommentModal from "widgets/Modal/CommentModal/CommentModal"

const CommentInputBox = () => {
    const [showModal, setShowModal] = useState(false);
    const [comment, setComment] = useState('');

    useEffect(() => {
        if (showModal) {
            document.body.classList.add('modal-open');
        } else {
            document.body.classList.remove('modal-open');
        }
    }, [showModal]);

    const handleOpenModal = () => {
        setShowModal(true);
    };


    const handleSubmitComment = () => {
        // 댓글 처리 로직 추가
    };

    const handleCommentChange = (e) => {
        setComment(e.target.value);
    };

    return (
        <div className="comment-inputBox" onClick={showModal ? null : handleOpenModal}>

            <div className="comment-inputBox__wrapper">
                <div className="emoticon-box">
                    <div className="emoticon-icon">
                        <span className="material-icons button-icon">
                          sentiment_satisfied_alt
                        </span>
                    </div>
                </div>
                <div className="inputBox">
                    <input
                        type="text"
                        className="textForm"
                        placeholder="자유롭게 댓글 달아주세요."
                        value={comment}
                        onChange={handleCommentChange}
                        disabled={!showModal}
                    />
                </div>
                <div className="submit-comment">
                    <div className="submit-button" onClick={handleSubmitComment}>
                        <span className="material-icons">send</span>
                    </div>
                </div>
            </div>
            <CommentModal
                showModal={showModal}
                setShowModal={setShowModal}
            />
        </div>
    );
};

export default CommentInputBox;