import React from 'react';

const FloatingActionButton = ({ onClick }) => {
    return (
        <button className="floating-action-button" onClick={onClick}>
            <span className="material-icons">add</span>
        </button>
    );
}

export default FloatingActionButton;
