import React from 'react';
import getLevelData from "utils/user/getLevelData";

const UserProfile = ({ nickname , profile}) => {

    const { showLevel ,name, color } = getLevelData(profile.level);
    const levelColor = color ? `#${color}` : '#000000'; // 기본 색상은 검정색

    return (
        <div className= "userProfile">
            <div className= "userProfileImage">
                <div className = "userProfileImage__Wrapper">
                    <img src="/default_profile.jpg"
                         alt="default image" />
                </div>
            </div>

            <div className="userDescription">
                <div className="user_info">

                    <div className="nickname">
                        <div className="nickname__wrapper">
                            <div className="upn-1">
                                {nickname}
                            </div>
                        </div>
                    </div>

                    <div className="user_level">
                        <div className="user_level__wrapper">
                            <div className="level " style={{ color: levelColor }}>
                                {showLevel} {name}
                            </div>
                        </div>
                    </div>
                </div>

                <div className="motto__wrapper">
                    <div className="mto-1">
                        팔로우 추가해보세요.
                    </div>
                </div>
            </div>
        </div>
    )
};


export default UserProfile;