import React, { useState, useEffect } from 'react';
import UserSelector from 'features/UserSelectorModal/components/UserSelector';

const UserSelectorList = ({ followedUsers, onUserSelectionChange , handleUserSelect , selectedUserIds }) => {

    return (
        <div className="user-selector-list">
            <ul className="selectors">
                {followedUsers.map((user) => (
                    <li key={user.userId}>
                        <UserSelector
                            user={user}
                            isSelected={selectedUserIds.includes(user.userId)}
                            onSelectUser={() => handleUserSelect(user.userId)}
                        />
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default UserSelectorList;
