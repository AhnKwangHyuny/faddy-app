import React from 'react';

const TitleInput = ({ title, setTitle }) => {
    return (
        <div className="title-input-container">
            <input
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="제목"
                className="title-input"
            />
        </div>
    );
};

export default TitleInput;
