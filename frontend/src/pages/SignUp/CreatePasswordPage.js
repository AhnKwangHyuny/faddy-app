import React, { useState , useContext } from 'react';
import * as Style from "Common/SignUpStyle";
import { useNavigate } from 'react-router-dom';
import SignupContext from "./SignUpContext";

function PasswordInputForm() {
    const {password , setPassword } = useContext(SignupContext);

    const [rePassword , setRePassword] = useState('');

    const [value , setValue ] = useState('');

    const [error, setError] = useState('');
    const [message, setMessage] = useState('');

    const [reError , setReError] = useState('');
    const [reMessage , setReMessage] = useState('');

    const [isAvailable, setIsAvailable] = useState(false);

    const navigate = useNavigate();

    const onChangePasswordHandler = (event) => {

        const password = event.target.value;

        if(password === '') {
            setError('');
            setMessage('');

            setValue('');
            return false;
        }

        setValue(password);

        if(password == "") {
            return;
        }

        const isValid = isValidPassword(password);

        if(isValid) {
            setPassword(password);
            setError("");
        }

        const message = isValid ? "사용가능한 비밀번호 입니다." : "비밀번호는 숫자,영문자, 특수문자롤 1개씩 포함한 8자리 이상이어야 합니다.";
        isValid ? setMessage(message) : setError(message);


    }

    const onChangeRePasswordHandler = (e) => {
        const newRePassword = e.target.value;

        setRePassword(newRePassword);

        if (password === newRePassword && isValidPassword(newRePassword)) {

            setReError('');
            setReMessage("비밀번호가 일치합니다.");

            setIsAvailable(true);

        } else {
            setReError("비밀번호가 일치하지 않습니다.");
            setIsAvailable(false);
        }
    };


    const isValidPassword = (password) => {
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;
        return passwordRegex.test(password);
    }

    const onFormSubmit = (event) => {
        event.preventDefault();

        setPassword(value);

        navigate('/signup/nickname');
    };

    const DisplayMessage = ({ error, message }) => { // message or error 출력
        if (error) {
            return <Style.ErrorMessage>{error}</Style.ErrorMessage>;
        } else if (message) {
            return <Style.Message>{message}</Style.Message>;
        }

        return null;
    };

    return (
        <form onSubmit={onFormSubmit}>
            <Style.Instruction>비밀번호를 입력해주세요.</Style.Instruction>
            <div>
            <Style.InputField
                onChange={onChangePasswordHandler}
                type="password"
                id='password'
                name='password'
                value={value}
                theme='underLine'
                placeholder="숫자,영문자,특수문자를 1개 이상 사용한 8자리 이상의 비밀번호"
            />
            </div>

            <DisplayMessage error ={error} message ={message} />


            <Style.InputField
                onChange={onChangeRePasswordHandler}
                type="password"
                id='password'
                name='password'
                value={rePassword}
                theme='underLine'
                placeholder="숫자,영문자,특수문자를 1개 이상 사용한 8자리 이상의 비밀번호"
            />

             <DisplayMessage error={reError} message ={reMessage} />


            <Style.StepInfo>2 of 3</Style.StepInfo>

            <Style.ProgressSection>
                <Style.ProgressBar/>
            </Style.ProgressSection>


            <Style.NextButton disabled={!isAvailable} type = "submit"> 다음 </Style.NextButton>

            <Style.FooterIndicator />
        </form>
    );
}

export default PasswordInputForm;
