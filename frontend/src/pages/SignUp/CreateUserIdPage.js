import React, { useState , useEffect , useContext , useRef } from 'react';
import * as Style from "Common/SignUpStyle";
import { Link } from 'react-router-dom';
import SignUpContext from "./SignUpContext";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { checkUserId } from 'api/user/UserVerificationAPI'


function IdInputForm() {

  const {username, setUsername} = useContext(SignUpContext);

  const [idError, setIdError] = useState('');
  const [idMessage, setIdMessage] = useState('');
  const [isIdDuplicated, setIsIdDuplicated] = useState(false); // 아이디 중복 검사를 했는지 안했는지
  const [isIdAvailable, setIsIdAvailable] = useState(false); // 아이디 사용 가능한지 아닌지

  const idInputRef = useRef();
  const navigate = useNavigate();


  const onChangeIdHandler = (e) => {
    const userId = e.target.value;

    setUsername(userId);
    // id 유효성 검사
    checkId(userId);


  }

  const checkId = async (userId) => {
    const idRegex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{5,15}$/;

    if (userId === '' || !idRegex.test(userId)) {
      setIdError('아이디는 5~15자의 영소문자, 숫자만 입력 가능합니다.');
      setIsIdAvailable(false);
      return false;
    }

     // 아이디 중복 검사
     const result = checkUserId(userId);

     result.then((res) => {
       setIdError('');
       setIdMessage(res.data.responseMessage);

       setIsIdDuplicated(false);
       setIsIdAvailable(true);


     })
     .catch((err) => {


        setIdError(err.response.data.message);
        setIdMessage("");

        setIsIdAvailable(false);
     });


  }

  const idDuplicationCheck = async(userId) => {
    await axios.post("http://localhost:9000/users/check-duplication/userId" , {
        userId : userId,
    })
    .then((response) => {
        console.log(response);

        if(response?.data?.isDuplicated == null || response.data.message == null) {
            setIdError("잘못된 요청입니다.");
            return;
        }

        setIsIdAvailable(!response.data.isDuplicated);
        setIdMessage(response.data.message);

    })
    .catch(function(error) {

        setIdError("요청 처리 중 오류가 발생했습니다. 다시 시도해주세요.");

    })
  }

  const DisplayMessage = ({ idError, idMessage }) => { // message or error 출력
    if (idError) {
      return <Style.ErrorMessage>{idError}</Style.ErrorMessage>;
    } else if (idMessage) {
      return <Style.Message>{idMessage}</Style.Message>;
    }
      return null;
    };

  const onFormSubmit = (event) => {
    event.preventDefault();
    const id = idInputRef.current.value;

    // history.push()를 사용하여 원하는 URL로 라우팅합니다.
    if(isIdAvailable) {
        navigate('/signUp/password');
    }


  };

  return (
    <>
    <Style.Instruction>아이디를 입력해주세요.</Style.Instruction>

    <Style.InputField
        ref = {idInputRef}
        onChange={onChangeIdHandler}
        type="text"
        id='id'
        name='id'
        value={username}
        placeholder='아이디 입력'
        theme='underLine'
        maxLength={15}
        />

    <DisplayMessage idError={idError} idMessage={idMessage} />


    <Style.StepInfo>1 of 3</Style.StepInfo>

    <Style.ProgressSection>
        <Style.ProgressBar />
    </Style.ProgressSection>

    <form onSubmit = {onFormSubmit}>
      <Style.NextButton disabled={!isIdAvailable} type = "submit"> 다음 </Style.NextButton>
    </form>

    <Style.FooterIndicator />

    </>
  );
}

export default IdInputForm;
