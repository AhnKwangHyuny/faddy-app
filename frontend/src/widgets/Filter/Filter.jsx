import React, { useEffect, useState } from 'react';

const Filter = ({ labelHide = false , category_list, selectedCategories, setSelectedCategories }) => {
    const [selectedCategoryStates, setSelectedCategoryStates] = useState({});

    useEffect(() => {
        // 카테고리 데이터를 받아왔을 때, 각 카테고리에 대한 selectedState 초기화
        const initialSelectedStates = {};
        Object.keys(category_list).forEach((categoryKey) => {
            initialSelectedStates[categoryKey] = null; // 선택된 아이템이 없음을 나타내기 위해 null을 사용
        });
        setSelectedCategoryStates(initialSelectedStates);
    }, [category_list]);

    const handleItemClick = (categoryKey, value) => {
        setSelectedCategoryStates((prevSelectedStates) => {
            const updatedSelectedStates = { ...prevSelectedStates };
            const isAlreadySelected = updatedSelectedStates[categoryKey] === value;

            // 클릭된 아이템에 대한 상태 업데이트
            updatedSelectedStates[categoryKey] = isAlreadySelected ? null : value;

            const newSelectedCategories = { ...selectedCategories };

            // 상태 업데이트에 따른 selectedCategories 업데이트
            if (updatedSelectedStates[categoryKey] === null) {
                delete newSelectedCategories[categoryKey];
            } else {
                newSelectedCategories[categoryKey] = updatedSelectedStates[categoryKey];
            }
            setSelectedCategories(newSelectedCategories);
            // DOM을 직접 조작하여 selected 클래스 추가 또는 제거
            setTimeout(() => {
                document.querySelectorAll(`.category__container`).forEach(container => {
                    if(container.querySelector('.logo').textContent.trim() === categoryKey){
                        container.querySelectorAll('.switch-button').forEach(button => {
                            button.classList.remove('selected');
                        });
                    }
                });

                // 현재 선택된 아이템에만 'selected' 클래스 추가
                if (!isAlreadySelected) {
                    document.querySelectorAll(`.category__container`).forEach(container => {
                        if(container.querySelector('.logo').textContent.trim() === categoryKey){
                            container.querySelectorAll('.switch-button').forEach(button => {
                                if (button.querySelector('.item').textContent === value) {
                                    button.classList.add('selected');
                                }
                            });
                        }
                    });
                }
            }, 0);

            return updatedSelectedStates;
        });
    };

    return (
        <div className="filter">
            <div className="filter__wrapper">
                {!labelHide && (
                    <div className="category__name">카테고리</div>
                )}
                {Object.keys(category_list).some((key) => category_list[key].length > 0) && (
                    <>
                        {Object.keys(category_list).map((categoryKey) => (
                            category_list[categoryKey] && category_list[categoryKey].length > 0 && (
                                <ul key={categoryKey} className="filter__list">
                                    <li>
                                        <div className="category__container">
                                            <div className="category_key">
                                                <div className="logo">{categoryKey}</div>
                                            </div>
                                            <div className="category-list">
                                                {category_list[categoryKey].map((value, index) => (
                                                    <div
                                                        key={index}
                                                        // className={`switch-button ${selectedCategoryStates[categoryKey] === value ? 'selected' : ''}`}
                                                        className={`switch-button `}
                                                        onClick={() => handleItemClick(categoryKey, value)}
                                                    >
                                                        <span className="item">{value}</span>
                                                    </div>
                                                ))}
                                            </div>
                                        </div>
                                    </li>
                                </ul>
                            )
                        ))}
                    </>
                )}
            </div>
        </div>
    );
};

export default Filter;
