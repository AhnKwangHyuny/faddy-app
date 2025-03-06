import {UploadImage} from "utils/Image/ImageUtils";
import {useRef, useState} from "react";
import {deleteImage} from "api/delete";

const ImageUploader = ({imageList , setImageList}) => {

    const imageInputRef = useRef(null);

    //frame 클릭 시 이미지 input click
    const handleCLickUploader = () => {
        imageInputRef.current.click();
    };

    /**이미지 서버로 전송 api*/
    const handleUploadImage = async (event) => {
        const config = await UploadImage(event , "snap");

        const uploadedImageInfo = config.data;

        if (!uploadedImageInfo || typeof uploadedImageInfo !== 'object') {
            return alert("사진을 불러오는데 실패했습니다. ");
        }

        setImageList((prev) => [...prev, uploadedImageInfo]);
    };

    //이미지 삭제 메소드
    const handleDeleteImage = (imageToDelete) => {

        // 이미지 삭제 요청
        const request = {
            hashedName: imageToDelete.hashedName,
            url: imageToDelete.url
        };
        deleteImage(request)
            .then((response) => {
                console.log(response);
            })
            .catch((error) => {
                console.log(error);
                return;
            });

        setImageList((prev) => prev.filter((image) => image.hashedName !== imageToDelete.hashedName));
    };

    return (
        <div className="image-upload__container">
            {imageList.length === 0 && (
                <div className="image__uploader" onClick={handleCLickUploader} onTouchStart={handleCLickUploader}>
                    <div className="uploader__frame">
                        <input
                            type="file"
                            className="Image__Input"
                            onChange={handleUploadImage}
                            ref = {imageInputRef}
                        />
                    </div>

                    <div className="cross-container">
                        <div className="cross"></div>
                    </div>
                </div>
            )}

            <ul className="image-upload__list">
                {imageList.map((image, index) => (
                    <li key={index} className="image-upload__item">
                        <div>
                            <img src={image.url} alt={image.name} style={{width: "100px", height: "100px"}}/>
                            <button onClick={() => handleDeleteImage(image)}>삭제</button>
                        </div>
                    </li>
                ))}

                {imageList.length > 0 && imageList.length < 6 && (
                    <li className="image-upload__item">
                        <div className="image-upload__button">

                            <div className="cross-container">
                                <div className="cross"></div>
                            </div>

                            <input
                                type="file"
                                className="image-input"
                                onChange={handleUploadImage}
                            />
                        </div>
                    </li>
                )}


            </ul>
        </div>
    );
};

export default ImageUploader;