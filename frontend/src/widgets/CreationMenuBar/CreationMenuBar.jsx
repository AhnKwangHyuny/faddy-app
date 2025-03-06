import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faAngleLeft } from '@fortawesome/free-solid-svg-icons';
import { useNavigate, useLocation } from 'react-router-dom';
import { deleteImageList } from "utils/Image/ImageUtils";

const CreationMenuBar = ({
    imageList,
    description,
    tags,
    selectedCategories,
    onUploadSnap
}) => {
    const navigate = useNavigate();
    const location = useLocation();

    const handleBackUrl = () => {
        // /snaps URL인 경우에만 이미지 리스트 삭제 시도
        if(location.pathname === "/snaps") {
            deleteImageList(imageList)
                .then(response => {
                    return navigate(-1);
                })
                .catch(error => {
                    console.log(error);
                    return alert("이미지 삭제 중 에러가 발생했습니다.");
                });
        } else {
            // /snaps URL이 아닌 경우, 바로 이전 페이지로 이동
            navigate(-1);
        }
    };

    const handleUploadButtonClick = () => {
        onUploadSnap();
    };

    return (
        <header className="Creation__menu__bar">
            <div className="menu__bar__container">
                <div className="frame__menu">
                    <div className="back__icon__wrapper">
                        <FontAwesomeIcon
                            icon={faAngleLeft}
                            className="back-icon__button"
                            onClick={handleBackUrl}
                            style={{
                                touchAction: 'none',
                                userSelect: 'none',
                                WebkitTapHighlightColor: 'transparent',
                            }}
                        />
                    </div>
                    <div className="title">등록하기</div>
                    <div className="submit-button" onClick={handleUploadButtonClick}>
                        완료
                    </div>
                </div>
            </div>
        </header>
    );
};

export default CreationMenuBar;