import React, { useEffect } from 'react';
import { Route, useNavigate } from 'react-router-dom';

import {checkTokenExpiration} from 'utils/auth/checkTokenExpiration';

const AuthCheckRoute = ({ children }) => {
    const token = localStorage.getItem('authToken'); // 로컬 스토리지에서 토큰을 가져옵니다.
    const navigate = useNavigate();

    useEffect(() => {
        if (!checkTokenExpiration(token)) {

            alert("접근 권한이 없습니다. [3000]");

            navigate('/login');
        }
    }, []);

     if (!localStorage.getItem('authToken')) {
            return null;
        }

    return children; // 토큰이 있으면 원래 컴포넌트를 렌더링합니다.
};

export default AuthCheckRoute;
