import React from 'react';
const TextArea = ({ imageList, textData }) => {
    return (
        <div className="textArea">
            {/* 이미지 영역 */}
            {imageList && imageList.length > 0 && (
                <div className="image-wrapper">
                    {imageList.map((imageUrl) => (
                        <img src={imageUrl} alt="SNS 이미지" key={imageUrl} />
                    ))}
                </div>
            )}

            {/* 텍스트 영역 */}
            <div className="description-wrapper">
                {textData.split('\n').map((line, index) => (
                    <div key={index}>
                        {line === '' ? <br/> : line}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default TextArea;