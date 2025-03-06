import React from 'react';
import {Link, useNavigate} from 'react-router-dom';


const Logo = ({title}) => {
    const navigate = useNavigate();
    const handleOnClick = () => {
        navigate("/StyleShare");
    };

    return (
        <div className="logo">
            <div className='logo__wrapper'>
                <Link to='/StyleShare'>
                    <span onClick={handleOnClick}>{title}</span>
                </Link>
            </div>

        </div>
    );
}

export default Logo;
