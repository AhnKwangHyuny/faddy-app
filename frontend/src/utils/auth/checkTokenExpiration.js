import {jwtDecode} from 'jwt-decode';

export const checkTokenExpiration = (token) => {

    if (!token) {
        return false;
    }

    const decodedToken = jwtDecode(token);

    const currentTime = Date.now() / 1000; // 현재 시간을 초 단위로 변환합니다.

    if (decodedToken.exp < currentTime) {

        // 토큰이 만료됨.

        localStorage.removeItem('authToken');

        return false;

    } else {
        // 토큰 유효함
        return true;
    }
};
