import React from 'react';
import { useNavigate } from 'react-router-dom';

const CreateTopicModal = ({ showModal, setShowModal }) => {
    const navigate = useNavigate();

    const handleClose = () => {
        setShowModal(false);
    };

    const handleButtonClick = (topic) => {
        navigate(`/styleBoards/create?topic=${topic}`);
    };

    return (
        <div className={`create-topic-modal ${showModal ? 'open' : ''}`}>
            <div className="create-topic-modal__wrapper">
                <div className="modal-touch-bar">
                    <div className="touch-bar"></div>
                </div>
                <div className="create-topic-modal__header">
                    <div className="close-button" onClick={handleClose}>
                        <span className="material-icons">close</span>
                    </div>
                    <div className="modal-title__container">
                        <span className="title">주제를 선택해주세요.</span>
                    </div>
                </div>
                <div className="create-topic-modal__content">
                    <button className="topic-button" onClick={() => handleButtonClick('question')}>
                        <span className="material-icons">help_outline</span>
                        질문하기
                    </button>
                    <button className="topic-button" onClick={() => handleButtonClick('daily')}>
                        <span className="material-icons">today</span>
                        일상공유
                    </button>
                    <button className="topic-button" onClick={() => handleButtonClick('fashion')}>
                        <span className="material-icons">checkroom</span>
                        패션공유
                    </button>
                    <button className="topic-button" onClick={() => handleButtonClick('tips')}>
                        <span className="material-icons">lightbulb</span>
                        패션꿀팁
                    </button>
                </div>
            </div>
        </div>
    );
};

export default CreateTopicModal;
