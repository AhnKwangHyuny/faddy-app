import React, {useEffect, useState} from 'react';
import {UploadImage , delImage} from "utils/Image/ImageUtils";
import ImageUploadIcon from "shared/ui/icons/ImageUploadIcon/ImageUploadIcon";


const ContentBox = ({icon,placeholder = "자유롭게 글 작성해주세요.",onSubmit}) => {
    const [content, setContent] = useState('');

    const handleCommentChange = (e) => {
        setContent(e.target.value);
    };

    const handleOnSubmit = () => {
        onSubmit(content);

        // input box 초기화
        setContent("");
    }

    // Image upload icon이 chat를 보내는 경우
    const handleCreateChatImage = async (event, category) => {

        try {
            // 이미지 업로드
            const image = await UploadImage(event, category);

            // 이미지 response가 없는 경우 에러 메세지 발송 (추후 구현)
            if (!image || !image.data) {
                return;
            }

            const imageObjUrl = image.data.url;
            console.log(imageObjUrl);

            // 이미지 response가 없는 경우 에러 메세지 발송 (추후 구현)
            if(!imageObjUrl) {
                // 생성된 이미지 삭제 요청
                await delImage(image);
                return;
            }

            // 이미지 url을 chat content에 저장
            onSubmit(imageObjUrl);


        } catch (e) {
            console.error(e);
            return;
        }

    }

    return (
        <div className="content-inputBox">
            <div className="content-inputBox__wrapper">
                <ImageUploadIcon onSubmit = {handleCreateChatImage} category = {"chat"}/>
                <div className="inputBox">

                    <input
                        type="text"
                        className="textForm"
                        placeholder={placeholder}
                        value={content}
                        onChange={handleCommentChange}
                    />
                </div>
                <div className="submit-content">
                    <div className="submit-button" onClick={handleOnSubmit}>
                        <span className="material-icons">send</span>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ContentBox;