import React, { useState } from 'react'

import Logo from "shared/ui/Logo/Logo"
import {BackButton} from "widgets/Button/Button"
import MenuIcons from "pages/SnapDetail/Components/MenuIcons/MenuIcons.jsx";
import {SubmitButton} from "shared/ui/Button/SubmitButton";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAngleLeft} from '@fortawesome/free-solid-svg-icons';
import OptionModal from "pages/styleBoardDetailPage/Components/OptionModal";

const Header = ({ onBackButtonClick, onOptionButtonClick, isOwner }) => {
    const [isModalOpen, setIsModalOpen] = useState(false);

    const handleOptionButtonClick = () => {
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
    };

    return (
        <header className="header">
            <div className="header-container">
                <div className="frame__menu">
                    <div className="back__icon__wrapper" onClick={onBackButtonClick}>
                        <FontAwesomeIcon icon={faAngleLeft} />
                    </div>
                    <div className="title">
                        <span>StyleBoard</span>
                    </div>
                    <div className="option-button_container">
                        {isOwner && (
                            <span
                                className="material-symbols-outlined option-button"
                                style={{ color: '#000000' }}
                                onClick={handleOptionButtonClick}
                            >
                                more_vert
                            </span>
                        )}
                    </div>
                </div>
            </div>
            {isModalOpen && (
                <OptionModal onClose={closeModal} onOptionButtonClick={onOptionButtonClick} />
            )}
        </header>
    );
};

export default Header;

