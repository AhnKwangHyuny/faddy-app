import React from 'react';

const Header = ({ onClose, onDone, isActive }) => {
    return (
        <div className="modal-header">
            <button onClick={onClose} className="close-modal">
                <span className="material-icons">close</span>
            </button>
            <div className="modal-title">
                <span className="title">#대화상대 선택</span>
            </div>
            <button
                className={`submit-button ${isActive ? 'enabled' : ''}`}
                onClick={onDone}
                disabled={!isActive}
            >
                완료
            </button>
        </div>
    );
};

export default Header;
