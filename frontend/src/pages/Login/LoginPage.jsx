import React, { useRef, useState, useEffect } from 'react';
import './_test.scss';
import default_profile from 'shared/img/default_profile.jpg';
import google_logo from 'shared/img/social_login_logo/google_logo.png';
import kakao_logo from 'shared/img/social_login_logo/kakaoTalk_logo.png';
import naver_logo from 'shared/img/social_login_logo/naver_logo.png';
import instagram_logo from 'shared/img/social_login_logo/instagram_logo.png';
import { initiateLocalStorage } from 'utils/auth/authUtils';
import { Link, useNavigate } from 'react-router-dom';
import { ValidatePassword, ValidateUsername } from 'utils/Validate';
import { postUserLogin } from 'api/post';
import { getUserId } from 'api/get';
import { useAuth } from 'shared/context/AuthContext';

const LoginPage = () => {

    useEffect(() => {
        initiateLocalStorage("ACCESS_TOKEN", "REFRESH_TOKEN", "userId", "GRANT_TYPE", "username");
    }, []);

    const navigate = useNavigate();
    const usernameRef = useRef();
    const passwordRef = useRef();
    const [error, setError] = useState('');
    const { login } = useAuth();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const username = usernameRef.current.value;
        const password = passwordRef.current.value;

        if (username === "") {
            setError("아이디를 입력해주세요");
            return false;
        }

        if (!ValidateUsername(username) || !ValidatePassword(password)) {
            setError("아이디(로그인 전용 아이디) 또는 비밀번호를 잘못 입력했습니다. 입력하신 내용을 다시 확인해주세요.");
            return false;
        }

        setError("");

        try {
            const response = await postUserLogin({ username, password });
            const config = response.data;
            console.log(response);

            if (config.grantType !== "Bearer" || config.accessToken === undefined) {
                alert("오류 발생 다시 로그인 부탁드립니다.");
                navigate(0);
                return;
            }

            localStorage.setItem("ACCESS_TOKEN", config.accessToken);
            localStorage.setItem("REFRESH_TOKEN", config.refreshToken);
            localStorage.setItem("GRANT_TYPE", config.grantType);

            const userIdResponse = await getUserId(username);
            const userId = userIdResponse.data.data;

            localStorage.setItem("userId", userId);
            localStorage.setItem("username", username);
            login(userId);

            navigate("/styleShare");
        } catch (error) {
            console.log(error);
            alert("[error] 오류가 발생했습니다. 다시 로그인 부탁드립니다.");
        }

        return false;
    };

    const DisplayMessage = ({ error }) => {
        if (error) {
            return <div className="error-message">{error}</div>;
        }
        return null;
    };

    return (
        <div className="login-container">
            <div className="brand-name">Faddy</div>
            <h2 className="login-title">회원 로그인</h2>
            <form className="login-form" onSubmit={handleSubmit}>
                <input type="text" placeholder="아이디" ref={usernameRef} />
                <input type="password" placeholder="비밀번호" ref={passwordRef} />
                <button type="submit" className="login-button">로그인</button>
                <DisplayMessage error={error} />
                <div className="options">
                    <label className="checkbox-container">
                        <input type="checkbox" />
                        <span className="checkmark"></span> 로그인 유지
                    </label>
                    <div className="links">
                        <Link to="/signup" className="signup-link">회원가입</Link>
                        <span className="divider-vertical"></span>
                        <Link to="#" className="find-id-password">정보찾기</Link>
                    </div>
                </div>
            </form>
            <div className="divider">
                <span>또는</span>
            </div>
            <div className="social-login">
                <div className="social-login-title">
                    <span>SNS계정으로 로그인</span>
                </div>
                <div className="social-login-buttons">
                    <button className="social-button">
                        <img src={naver_logo} alt="Naver Login" />
                    </button>
                    <button className="social-button">
                        <img src={kakao_logo} alt="Kakao Login" />
                    </button>
                    <button className="social-button">
                        <img src={google_logo} alt="Google Login" />
                    </button>
                    <button className="social-button">
                        <img src={instagram_logo} alt="Instagram Login" />
                    </button>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;
