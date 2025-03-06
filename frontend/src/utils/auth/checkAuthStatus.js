import {verifyToken} from "api/get";
import {reissueToken} from "api/post";


/**
 * 현재 유저의 auth token 유효성 상태 검증
 * */

/** refresh token 검증 후 accessToken 재발급 요청*/


export const checkAuthStatus = async (navigate) => {
    const accessToken = localStorage.getItem('ACCESS_TOKEN');
    const refreshToken = localStorage.getItem('REFRESH_TOKEN');

    try {
        if (!accessToken) {
            const isRefreshed = await handleTokenRefresh(refreshToken);
            if (!isRefreshed) {
                navigate('/login');
                return false;
            }
        } else {
            const isAccessTokenValid = await isVerifiedToken(accessToken);
            if (!isAccessTokenValid) {
                const isRefreshed = await handleTokenRefresh(refreshToken);
                if (!isRefreshed) {
                    navigate('/login');
                    return false;
                }
            }
        }
        return true;
    } catch (error) {
        clearTokens();
        navigate('/login');
        return false;
    }
};

const refreshTokenAndSaveNewTokens = async (refreshToken) => {
    try {
        const response = await reissueToken(refreshToken);
        if (response.status !== 200) {

            return false;
        }
        const { accessToken, refreshToken: newRefreshToken } = response.data;
        saveTokens(accessToken, newRefreshToken);
        return true;
    } catch (error) {
        console.error('Failed to refresh token:', error);
        return false;
    }
};

const isVerifiedToken = async (accessToken) => {
    try {
        const response = await verifyToken(accessToken);

        if(response.status === 200) {
            return true;
        }

        return false;

    } catch (error) {
        return false;
    }
};

const saveTokens = (accessToken, refreshToken) => {
    localStorage.setItem('ACCESS_TOKEN', accessToken);
    localStorage.setItem('REFRESH_TOKEN', refreshToken);
};

const clearTokens = () => {
    localStorage.removeItem('ACCESS_TOKEN');
    localStorage.removeItem('REFRESH_TOKEN');
};

const handleTokenRefresh = async (refreshToken) => {
    if (!refreshToken) {
        clearTokens();
        return false;
    }

    const isTokenRefreshed = await refreshTokenAndSaveNewTokens(refreshToken);
    if (!isTokenRefreshed) {
        clearTokens();
        return false;
    }

    return true;
};

