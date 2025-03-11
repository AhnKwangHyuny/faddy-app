import React, {createContext, useEffect, useState} from 'react'

export const LoginContext = createContext(undefined);
LoginContext.displayName = 'LoginContextName';

const LoginContextProvider = ({children}) => {

    /*
    *  상태
    *  - 로그인 여부
    *  - 유저 정보
    * */

    const [isLogin , setIsLogin] = useState(false);

    const[userInfo , setUserInfo] = useState({});

    const [rememberUserId , setRememberUserId] = useState();

    //로그인 세팅
    const loginSetting = (userData , accessToken) => {

        const {no , userId} = userData;

        // 로그인 상태 저잦
        setIsLogin(true)

        //로그인 유저 정보 세팅
        const updatedUserInfo = {no  , userId};
        setUserInfo(updatedUserInfo)
    }

    const logoutSetting = () => {
        // 로그인 여부 : false
        setIsLogin(false);

    }

    const  logout = () => {
        setIsLogin(false);

        //유저 정보 초기화
        setUserInfo(null);

        //localStorage 초기화
        window.localStorage.removeItem("access_token");
        window.localStorage.removeItem("refresh_token");
    }

    useEffect(()=> {

    } , []);

    return (
        <LoginContext.Provider value = {{isLogin , logout}}>
            {children}
        </LoginContext.Provider>
    )

}
