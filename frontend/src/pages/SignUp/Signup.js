// 필요한 컴포넌트와 훅을 임포트합니다.
import { SignupProvider } from './SignUpContext';
import IdInputForm from './CreateUserIdPage';
import PasswordInputForm from './CreatePasswordPage';
import NicknameInputForm from './CreateNicknamePage';
import EmailVerificationForm from './EmailVerificationPage';
import { BrowserRouter as Router, Route, useNavigate, Routes , useLocation } from 'react-router-dom';
import { useEffect } from 'react';

// AuthCheckRoute 컴포넌트를 임포트합니다.
import AuthCheckRoute from 'routes/auth/AuthCheckRoute';

// SignUp 컴포넌트
const SignUp = function () {
  // react-router-dom의 useNavigate와 useLocation 훅을 사용합니다.
  const navigate = useNavigate();
  const location = useLocation();

  // 현재 경로가 '/signup'인 경우 'email/verifications'로 이동하는 useEffect 훅을 사용합니다.
  useEffect(() => {
    if (location.pathname === '/signup') {
      navigate('email/verifications');
    }
  }, [navigate]);

  // SignUp 컴포넌트를 렌더링합니다.
  return (
    <SignupProvider>
      <Routes>
        {/* ID 입력 폼을 위한 라우트 */}
        <Route path="id" element={<AuthCheckRoute><IdInputForm /></AuthCheckRoute>} />
        {/* 비밀번호 입력 폼을 위한 라우트 */}
        <Route path="password" element={<AuthCheckRoute><PasswordInputForm /></AuthCheckRoute>} />
        {/* 별명 입력 폼을 위한 라우트 */}
        <Route path="nickname" element={<AuthCheckRoute><NicknameInputForm /></AuthCheckRoute>} />
        {/* 이메일 인증 폼을 위한 라우트 */}
        <Route path="email/verifications" element={<EmailVerificationForm />} />
      </Routes>
    </SignupProvider>
  );
}

// SignUp 컴포넌트를 내보냅니다.
export default SignUp;
