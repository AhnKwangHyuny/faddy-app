import React from 'react';
import {getLevelInfo} from "data/LevelData";

const MetaInfo = ({ userProfile, interaction }) => {

    const levelInfo = getLevelInfo(userProfile.level);

    return (
        <div className="meta-info">
            <div className = "profile-image_container">
                <img src={userProfile.profileImageUrl} alt="프로필 이미지" className="profile-image" />
            </div>

            <div className = "userProfile-interaction_container">
                <div className="user-profile">
                    <span className="user-level" style={{ color: levelInfo.color }}>
                        {levelInfo.level} {levelInfo.name}
                    </span>
                    <span className="user-nickname">{userProfile.nickname}</span>
                    <span className="created-at">• {userProfile.createdAt}</span>
                </div>
                <div className="interaction">
                    <div className="interaction-icon">
                        <span className="material-symbols-outlined icon">visibility</span>
                        <span className="count">{interaction.views}</span>
                    </div>
                    <div className="interaction-icon">
                        <span className="material-symbols-outlined icon">thumb_up</span>
                        <span className="count">{interaction.likes}</span>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MetaInfo;
