import React, { useEffect, useState } from 'react';
import Filter from "widgets/Filter/Filter";
import { snapCategoryData, searchFilterData } from "data/category";

const CategorySelector = ({ selectedCategories, setSelectedCategories }) => {
    const [categoryData, setCategoryData] = useState([]);

    // styleBoard Modal에선 카테고리 label 가림
    const isLabelHide = window.location.pathname.startsWith('/styleBoards');

    useEffect(() => { // 현재 url 정보를 가져와 어느 카테고리 data를 loading할지 결정
        if (window.location.pathname.startsWith('/snaps')) {
            setCategoryData(snapCategoryData);
        } else {
            setCategoryData(searchFilterData);
        }
    }, []);

    const categoryProps = categoryData.reduce((acc, cur) => {
        acc[cur.label] = cur.subCategories;
        return acc;
    }, {});

    return (
        <div className="snap_category">
            <Filter
                labelHide={isLabelHide}
                category_list={categoryProps}
                selectedCategories={selectedCategories}
                setSelectedCategories={setSelectedCategories}
            />
        </div>
    );
};

export default CategorySelector;
