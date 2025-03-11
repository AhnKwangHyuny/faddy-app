import React from 'react';

const SortingMenuItem = ({ category, isSelected, onSelect }) => {
    const handleClick = () => {
        onSelect(category.id);
    };

    return (
        <div
            className={`sorting-menu-item ${isSelected ? 'selected' : ''}`}
            onClick={handleClick}
        >
            {category.label}
        </div>
    );
};

export default SortingMenuItem;