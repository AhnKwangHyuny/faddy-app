import React, { useState, useEffect } from 'react';
import HashTagBox from "pages/Snap/Components/HashTagBox/HashTagBox";
import CategorySelector from "pages/Snap/Components/SnapCategory/SnapCategory";

const SearchFilterModal = ({ showModal, setShowModal, setFilterOptions, onSubmit }) => {
    const [tags, setTags] = useState([]);
    const [activeTab, setActiveTab] = useState('tag');
    const [selectedCategories, setSelectedCategories] = useState({});

    const handleClose = () => {
        setShowModal(false);
    };

    const handleTabClick = (tab) => {
        setActiveTab(tab);
    };

    const handleSearchClick = () => {

        const filterOptions = {
            tags: tags,
            category: selectedCategories
        };

        handleClose();

        onSubmit(filterOptions); // Load data after setting filter options
    };

    // 모달이 열릴 때 상태 초기화
    useEffect(() => {
        if (showModal) {
            setTags([]);
            setSelectedCategories({});
        }
    }, [showModal]);

    return (
        <div className={`search-filter-modal ${showModal ? 'open' : ''}`}>
            <div className="search-filter-modal__wrapper">
                <div className="modal-touch-bar">
                    <div className="touch-bar"></div>
                </div>
                <div className="search-filter-modal__header">
                    <div className="close-button" onClick={handleClose}>
                        <span className="material-icons">close</span>
                    </div>
                    <div className="modal-title__container">
                        <span className="title">검색 옵션을 선택해주세요.</span>
                    </div>
                </div>
                <div className="search-filter-modal__tabs">
                    <div
                        className={`tab ${activeTab === 'tag' ? 'active' : ''}`}
                        onClick={() => handleTabClick('tag')}
                    >
                        태그
                    </div>
                    <div
                        className={`tab ${activeTab === 'category' ? 'active' : ''}`}
                        onClick={() => handleTabClick('category')}
                    >
                        카테고리
                    </div>
                </div>
                <div className="search-filter-modal__content">
                    {activeTab === 'tag' && (
                        <HashTagBox labelHide={true} tags={tags} setTags={setTags} />
                    )}
                    {activeTab === 'category' && (
                        <CategorySelector
                            selectedCategories={selectedCategories}
                            setSelectedCategories={setSelectedCategories}
                        />
                    )}
                </div>
                <div className="search-filter-modal__footer">
                    <button className="search-button" onClick={handleSearchClick}>검색</button>
                </div>
            </div>
        </div>
    );
};

export default SearchFilterModal;
