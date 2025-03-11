import React from 'react';
import getLevelData from 'utils/user/getLevelData';

const Avatar = ({ userInfo }) => {
   const { profileImageUrl, nickname, level } = userInfo;
   const { showLevel, name, color } = getLevelData(level);
   const levelColor = color ? `#${color}` : '#000000'; // 기본 색상은 검정색
   return (
       <div className="user-profile">
           <div className="user-profile__avatar">
               <img src={profileImageUrl} alt={nickname} className="user-profile__avatar-img" />
           </div>
           <div className="user-profile__name">{nickname}</div>
           <div className="user_level">
               <div className="user_level__wrapper">
                   <div className="level " style={{ color: levelColor }}>
                       {showLevel} {name}
                   </div>
               </div>
           </div>
       </div>
   );
};

export default Avatar;