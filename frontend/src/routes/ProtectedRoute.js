import React, {useEffect, useState} from 'react';
import {Navigate, useNavigate} from 'react-router-dom';
import {checkAuthStatus} from "utils/auth/checkAuthStatus";


// 인증된 사용자만 접근 가능한 컴포넌트를 위한 보호된 경로 컴포넌트
const ProtectedRoute = ({ children }) => {

    const [isAuthenticated , setIsAuthenticated ] = useState(null);
    const navigate = useNavigate();
    //토큰 검증 함수
    /**user authentication 유효성 검증 요청*/
    const verifyAuth = async () => {

        //유저 토큰 유효한지 확인 (로그인 상태 유지 : true , 토큰 만료 및 유효하지 않음 : false)
        const isVerified = await checkAuthStatus(navigate);

        //isAuthenticated 에 현재 유저 상태 반환
        setIsAuthenticated(isVerified);

    };

    useEffect(() => {

        verifyAuth();

    }, []);

    if (isAuthenticated === null) {
        // 인증 상태 확인 중...
        return <div>인증 확인 중...</div>;
    }

    // 유저 로그인 상태가 유효하면 children component 렌더링
    return isAuthenticated ? children : <Navigate to="/login" />;
};

export default ProtectedRoute;
