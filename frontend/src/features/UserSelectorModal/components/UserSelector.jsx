import React, { useState } from 'react';

const UserSelector = ({ user, isSelected, onSelectUser }) => {
    const [isChecked, setIsChecked] = useState(isSelected);

    const handleChange = (e) => {
        setIsChecked(e.target.checked);
        onSelectUser(user);
    };

    return (
        <div className="user-profile">
            <div className="user-info">
                <div className="user-image-container">
                    <img src={user.profileImageUrl} alt="profile" />
                </div>
                <div className="user-nickname">
                    <span className="user-name">{user.nickname}</span>
                </div>
            </div>
            <div className="user-checkbox">
                <input
                    type="checkbox"
                    checked={isChecked}
                    onChange={handleChange}
                />
            </div>
        </div>
    );
};

export default UserSelector;