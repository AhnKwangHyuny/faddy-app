import React from 'react';
import { useNavigate } from 'react-router-dom';

const StyleBoard = ({ data }) => {
    const { id, title, userProfile, interaction, createdAt, thumbnailImage , category } = data;
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(`/styleBoards/detail/${id}?topic=${category}`);
    };

    return (
        <div className="style-board" onClick={handleClick} style={{ cursor: 'pointer' }}>
            <div className="style-board-content">
                <div className="style-board-title">
                    <span className="title">{title}</span>
                </div>
                <div className="user-profile">
                    <span className="level">{userProfile.level}</span>
                    <span className="nickname">{userProfile.nickname}</span>
                    <span className="created-at"> · {createdAt}</span>
                </div>
                <div className="interaction">
                    <div className="interaction-icon">
                        <span className="material-symbols-outlined">visibility</span>
                        <span className="count">{interaction.views}</span>
                    </div>
                    <div className="interaction-icon">
                        <span className="material-symbols-outlined">thumb_up</span>
                        <span className="count">{interaction.likes}</span>
                    </div>
                    <div className="interaction-icon">
                        <span className="material-symbols-outlined">article</span>
                        <span className="count">{interaction.comments}</span>
                    </div>
                </div>
            </div>
            <div className="thumbnail-container">
                <img src={thumbnailImage || "default_profile.jpg"} alt={title} className="thumbnail-image" />
            </div>
        </div>
    );
}

export default StyleBoard;
