// SortingMenu.jsx
import React, { useState } from 'react';
import SubCategoryMenu from 'features/Sorting/SortingMenu/components/SubCategoryMenu/SubCategoryMenu';
import SortingMenuItem from 'features/Sorting/SortingMenu/components/SortingMenuItem/SortingMenuItem';
import {snapCategoryData} from "data/category";

const SortingMenu = () => {
    const [selectedCategory, setSelectedCategory] = useState(null);
    const [selectedSubCategories, setSelectedSubCategories] = useState([]);
    const [openCategory, setOpenCategory] = useState(null);

    const handleCategorySelect = (categoryId) => {
        setSelectedCategory(categoryId);
        setOpenCategory(openCategory === categoryId ? null : categoryId);
    };

    const handleSubCategorySelect = (subCategory) => {
        if (selectedSubCategories.includes(subCategory)) {
            setSelectedSubCategories(selectedSubCategories.filter((item) => item !== subCategory));
        } else {
            setSelectedSubCategories([...selectedSubCategories, subCategory]);
        }
    };

    return (
        <div className="sorting-menu">

            <div className="sorting-icon sorting-menu-item">
                <span className="material-icons">
                    sort
                </span>
            </div>

            {snapCategoryData.map((category) => (
                <div key={category.id}>
                    <SortingMenuItem
                        category={category}
                        isSelected={selectedCategory === category.id}
                        isOpen={openCategory === category.id}
                        onSelect={handleCategorySelect}
                    />
                    {openCategory === category.id && (
                        <SubCategoryMenu
                            subCategories={category.subCategories}
                            selectedSubCategories={selectedSubCategories}
                            onSubCategorySelect={handleSubCategorySelect}
                        />
                    )}
                </div>
            ))}
        </div>
    );
};

export default SortingMenu;
