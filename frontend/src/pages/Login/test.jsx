import React from 'react';
import './_test.scss'; // Import the SCSS file for styling
import default_profile from 'shared/img/default_profile.jpg'; // Import the image file
import google_logo from 'shared/img/social_login_logo/google_logo.png'; // Import the image file
import kakao_logo from 'shared/img/social_login_logo/kakaoTalk_logo.png'; // Import the image file
import naver_logo from 'shared/img/social_login_logo/naver_logo.png'; // Import the image file
import instagram_logo from 'shared/img/social_login_logo/instagram_logo.png'; // Import the image file

const Login = () => {
    return (
        <div className="login-container">
            <div className="brand-name">Faddy</div>
            <h2 className="login-title">회원 로그인</h2>
            <form className="login-form">
                <input type="text" placeholder="아이디" />
                <input type="password" placeholder="비밀번호" />
                <button type="submit" className="login-button">로그인</button>
                <div className="options">
                    <label className="checkbox-container">
                        <input type="checkbox" />
                        <span className="checkmark"></span> 로그인 유지
                    </label>
                    <div className="links">
                        <a href="#" className="signup-link">회원가입</a>
                        <span className="divider-vertical"></span>
                        <a href="#" className="find-id-password">정보찾기</a>
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
                        <img src={google_logo} alt="Facebook Login" />
                    </button>
                    <button className="social-button">
                        <img src={instagram_logo} alt="Google Login" />
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Login;
