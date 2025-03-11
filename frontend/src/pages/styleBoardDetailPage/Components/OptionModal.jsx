import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { deleteStyleBoard } from 'pages/styleBoardDetailPage/api/delete';

const OptionModal = ({ onClose }) => {
    const [show, setShow] = useState(false);
    const { id } = useParams();
    const [showConfirmation, setShowConfirmation] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        setShow(true);
    }, []);

    const handleClose = () => {
        setShow(false);
        setTimeout(onClose, 300);
    };

    const handleDeleteClick = () => {
        setShowConfirmation(true);
    };

    const handleConfirmDelete = async () => {
        setShowConfirmation(false);
        setShow(false);
        setTimeout(async () => {
            try {
                await deleteStyleBoard(id);
                alert('스타일보드가 성공적으로 삭제되었습니다.');
                navigate('/styleBoards');
            } catch (error) {
                console.error('Error deleting style board:', error);
                alert('스타일보드 삭제에 실패했습니다.');
            }
            onClose();
        }, 300);
    };

    const handleCancelDelete = () => {
        setShowConfirmation(false);
    };

    return (
        <div className="modal-overlay" onClick={handleClose}>
            {!showConfirmation && (
                <div
                    className={`modal-content ${show ? 'show' : 'hide'}`}
                    onClick={(e) => e.stopPropagation()}
                    onTouchStart={(e) => e.stopPropagation()} // 모바일 터치 이벤트 추가
                >
                    <button onClick={handleClose} className="close-button">
                        <span className="material-symbols-outlined">
                            close
                        </span>
                    </button>
                    <div className="modal-buttons">
                        <button onClick={() => window.location.href = `/styleBoards/edit/${id}`}>수정하기</button>
                        <button onClick={handleDeleteClick}>삭제하기</button>
                    </div>
                </div>
            )}
            {showConfirmation && (
                <div
                    className={`modal-content confirmation-modal`}
                    onClick={(e) => e.stopPropagation()}
                    onTouchStart={(e) => e.stopPropagation()} // 모바일 터치 이벤트 추가
                >
                    <p>정말 삭제하시겠습니까?</p>
                    <p>삭제된 데이터는 복구할 수 없습니다.</p>
                    <div className="confirmation-buttons">
                        <button onClick={handleConfirmDelete}>예</button>
                        <button onClick={handleCancelDelete}>아니오</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default OptionModal;
