import React from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {faMagnifyingGlass} from "@fortawesome/free-solid-svg-icons";
import {faBell, faUser} from "@fortawesome/free-regular-svg-icons";


const MenuIcons = () => {
    return (
        <div className="header__logo snap-detail-icons">
            <div className="component__top-menu">
                <ul className="top-menu_list">
                    <li className="list__item">
                        <Link to='/search'>
                            <div className="icon__box">
                                <FontAwesomeIcon icon={faMagnifyingGlass} />
                            </div>
                        </Link>
                    </li>

                    <li className="list__item">
                        <Link to='/profile'>
                            <div className="icon__box">
                                <FontAwesomeIcon icon={faUser} />
                            </div>
                        </Link>
                    </li>

                </ul>
            </div>
        </div>
    );
}

export default MenuIcons;