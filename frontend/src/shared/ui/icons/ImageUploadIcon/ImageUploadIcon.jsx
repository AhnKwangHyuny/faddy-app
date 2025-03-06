import React, { useRef } from 'react';
import imageCategory from 'data/imageCategory';

const ImageUploadIcon = ({ onSubmit , category }) => {
    const fileInputRef = useRef();

    const handleIconClick = () => {
        fileInputRef.current.click();
    };

    const handleFileChange = async (event) => {
        const file = event.target.files[0];
        const imageCategoryName = imageCategory[category] || imageCategory.etc;

        if (file && imageCategoryName) {
            // 이미지 저장 요청 api
            await onSubmit(event, imageCategoryName.toLowerCase());
        }
    };

    return (
        <div className="image-upload-icon__container">
            <span className="material-symbols-outlined icon" onClick={handleIconClick}>
                attach_file
            </span>
            <input
                type="file"
                ref={fileInputRef}
                style={{ display: 'none' }}
                onChange={handleFileChange}
                accept="image/*"
            />
        </div>
    );
};

export default ImageUploadIcon;
