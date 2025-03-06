import React, { useState, useEffect, useContext } from 'react';
import * as Style from "Common/SignUpStyle";
import { useNavigate } from 'react-router-dom';
import SignupContext from "./SignUpContext";

import { validateNickname } from 'utils/Validate';
import { getNicknameValidationResponse } from 'api/user/UserVerificationAPI';
import { signup } from 'features/signup/signup';

function NicknameInputForm() {
  const { username, password, email, nickname, setNickname } = useContext(SignupContext);
  const navigate = useNavigate();

  const [value, setValue] = useState('');
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [isAvailable, setIsAvailable] = useState(false);

  useEffect(() => {
    if (!username || !password || !email) {
      alert("누락된 회원 정보가 존재합니다.");
      navigate('/signup/password');
    }
  }, [username, password, email, navigate]);

  const onChangeNicknameHandler = (e) => {
    const name = e.target.value;
    setValue(name);

    if (name.trim() === '') {
      setError('');
      setMessage('');
      return;
    }

    if (!validateNickname(name)) {
      setError("3~12자리 사이 닉네임을 입력해주세요.");
      return false;
    }

    // nickname 중복 검사
    getNicknameValidationResponse(value)
      .then((response) => {
        if (response?.data?.responseCode !== "200") {
          setError("유효하지 않은 닉네임입니다.");
          setIsAvailable(false);
          return;
        }

        setError('');
        setNickname(value);
        setMessage(response.data.responseMessage);
        setIsAvailable(true);
      })
      .catch((err) => {
        console.log(err);
        setIsAvailable(false);
      });
  }

  const DisplayMessage = ({ error, message }) => {
    if (error) {
      return <Style.ErrorMessage>{error}</Style.ErrorMessage>;
    } else if (message) {
      return <Style.Message>{message}</Style.Message>;
    }
    return null;
  };

  const onFormSubmit = (event) => {
    event.preventDefault();

    // 회원가입 접근 권한 토큰 삭제
    localStorage.removeItem('authToken');

    const fields = { username, password, email, nickname };
    const blankFields = Object.keys(fields).filter(key => !fields[key]);

    if (blankFields.length > 0) {
      alert(`${blankFields.join(', ')}의 정보가 누락되었습니다. 다시 회원가입 진행 부탁드립니다.`);
      navigate("/email/verifications");
      return;
    }

    // 전부 존재한다면 서버에 회원가입 요청
    signup(fields)
      .then((response) => {
        if (response.data.data.username !== fields.username) {
          alert("죄송합니다. 알 수 없는 오류가 발생했습니다. 다시 진행해주시길 부탁드립니다.");
          navigate("/signup/email/verifications");
          return;
        }

        alert("방문해주셔서 감사합니다! 회원가입 완료 되었습니다! '\n' 로그인 부탁드립니다.");
        navigate("/login");

      })
      .catch((error) => {
        console.log(error);
        alert("누락된 회원가입 정보 또는 유효하지 않은 값이 존재합니다. 다시 진행해주세요.");
        navigate("/signup/email/verifications");

        return;
      });
  }

  return (
    <form onSubmit={onFormSubmit}>
      <Style.Instruction>닉네임을 입력해주세요.</Style.Instruction>
      <Style.InputField
        onChange={onChangeNicknameHandler}
        type="text"
        id='nickname'
        name='nickname'
        value={value}
        placeholder="닉네임 입력"
        theme='underLine'
        maxLength={15}
      />
      <DisplayMessage error={error} message={message} />
      <Style.StepInfo>3 of 3</Style.StepInfo>
      <Style.ProgressSection>
        <Style.ProgressBar />
      </Style.ProgressSection>
      <Style.NextButton type="submit" disabled={!isAvailable}>다음</Style.NextButton>
      <Style.FooterIndicator />
    </form>
  );
}

export default NicknameInputForm;
