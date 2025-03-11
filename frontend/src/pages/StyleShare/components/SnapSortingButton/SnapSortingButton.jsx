import React, { useState } from 'react';

const SnapSortingButton = () => {
    const [isCurrentClick , setIsCurrentClick] = useState(true);

    const handleRecommendClick = () => {
        setIsCurrentClick(!isCurrentClick);
        // 추천 정렬 로직 추가
    };

    const handleCurrentClick = () => {
        setIsCurrentClick(!isCurrentClick);
        // 최신 정렬 로직 추가
    };

    return (
        <div className="snap-sorting-buttons">
            <div className="snap-sorting__buttons__container">
                <div className="button__wrapper">
                    <button
                        className={`recommend-sorting ${isCurrentClick ? 'isClicked' : ''}`}
                        onClick={handleRecommendClick}
                    >
                        <span className="button-name">최신</span>
                    </button>
                </div>
                <div className="button__wrapper">
                    <button
                        className={`recent-sorting ${!isCurrentClick ? 'isClicked' : ''}`}
                        onClick={handleCurrentClick}
                    >
                        <span className="button-name">추천</span>
                    </button>
                </div>
            </div>
        </div>
    );
};

export default SnapSortingButton;