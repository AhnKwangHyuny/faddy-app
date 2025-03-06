import React from 'react';

const SubCategoryMenu = ({ subCategories, selectedSubCategories, onSubCategorySelect }) => {
    return (
        <div className="sub-category-menu">
            {subCategories.map((subCategory) => (
                <div
                    key={subCategory}
                    className={`sub-category-item ${selectedSubCategories.includes(subCategory) ? 'selected' : ''}`}
                    onClick={() => onSubCategorySelect(subCategory)}
                >
                    {subCategory}
                </div>
            ))}
        </div>
    );
};

export default SubCategoryMenu;