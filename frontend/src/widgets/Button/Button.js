import {useNavigate} from "react-router-dom";
import {faAngleLeft, faChevronLeft} from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import React from "react";

export const BackButton = ({
    label = '뒤로가기',
    className = '',
    onClick = null,
}) => {

    const navigate = useNavigate();
    const handleClick = () => {
        if(onClick) {
            onClick();
        }

        navigate(-1);
    }

    return (
        <div className={`back-button ${className}`}>
            <div className="back-button__wrapper">
                <button
                    type="button"
                    className="back-button"
                    onClick={handleClick}
                >
                    <FontAwesomeIcon
                        icon={faChevronLeft}
                        style={{
                            touchAction: 'none',
                            userSelect: 'none',
                            WebkitTapHighlightColor: 'transparent',
                        }}
                    />
                </button>
            </div>
        </div>
    );
};

