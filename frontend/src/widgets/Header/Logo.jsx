import React from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMagnifyingGlass  } from '@fortawesome/free-solid-svg-icons';
import { faBell , faUser } from '@fortawesome/free-regular-svg-icons';


const Logo = ({ toggleMenu }) => {
    return (
        <div className="header__logo">
            <div className='logo_container'>
                <Link to='/'>
                    <em aria-hidden='true' onClick={toggleMenu}></em>
                    <span>Faddy</span>
                </Link>
            </div>

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
                        <Link to='/notifications'>
                            <div className="icon__box">
                               <FontAwesomeIcon icon={faBell} />
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

export default Logo;
