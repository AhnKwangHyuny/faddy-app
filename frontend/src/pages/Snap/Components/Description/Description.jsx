import React, {useState} from 'react';

const Description = ({description , setDescription}) => {


    const handleDescriptionChange = (event) => {
        const inputValue = event.target.value;

        if(inputValue.length <=1500) {
            setDescription(inputValue);
        }

    };

    return (

        <div className="description">
            <div className="description__wrapper">
                <div className="description__header">
                    <div className="logo">Description</div>
                </div>

                <div className="description__input-wrapper">
                    <textarea
                        className="description__input"
                        placeholder="착용한 아이템 및 스냅 소개글 올려주세요."
                        value = {description}
                        onChange = {handleDescriptionChange}
                        maxLength = {1500}
                    ></textarea>
                </div>


                <div className="description__footer">
                    <div className="limited__text">
                        {description.length} / 1500
                    </div>

                </div>
            </div>
        </div>
    );
};

export default Description;