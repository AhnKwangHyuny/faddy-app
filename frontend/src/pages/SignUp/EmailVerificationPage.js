// 필요한 모듈과 컴포넌트를 import합니다.
import * as Style from "./style/tsf";
import React, {
    useState,
    useContext,
    useEffect
} from "react";
import SignupContext from "./SignUpContext";
import axios from "axios";
import {
    getEmailAuthCode
} from "api/email/getEmailAuthCode";
import {
    verifyAuthCodeAndRequestAuthToken,
    deleteAuthCode,
} from "api/auth/authTokenRequestAPI";
import {
    useNavigate
} from 'react-router-dom';

import {
    axiosInstance
} from 'api/axiosInstance';



import Timer from "utils/Timer";

function RegistrationForm() {
    // 상태변수 정의
    const {
        email,
        setEmail
    } = useContext(SignupContext);

    const [error, setError] = useState("");
    const [message, setMessage] = useState("");

    const [authCodeError, setAuthCodeError] = useState("");
    const [authCodeMessage, setAuthCodeMessage] = useState("");

    const [isIdDuplicated, setIsIdDuplicated] = useState(false);
    const [isEmailAvailable, setIsEmailAvailable] = useState(false);
    const [isAvailable, setIsAvailable] = useState(false);
    const [isButtonAvailable, setIsButtonAvailable] = useState(true);
    const [isProcessing, setIsProcessing] = useState(false);

    const [value, setValue] = useState("");
    const [authCode, setAuthCode] = useState("");

    const [showTimer, setShowTimer] = useState(false);

    const navigate = useNavigate();

    const AUTHENTICATION_TOKEN = 'authentication';

    // component mount 시 인증 코튼 삭제
    useEffect(() => {
        localStorage.removeItem(AUTHENTICATION_TOKEN);
    }, []);


    /*
     * 이메일 인증코드 요청 핸들러
     */
    const onEmailCodeRequest = (e) => {

        if (isProcessing) {
            alert("이미 해당 이메일로 인증번호 발급이 완료되었습니다.");
            return;
        }

        alert("이메일 인증 코드 발송 완료했습니다. 만약 해당 메일로 인증 코드가 안 왔을 경우 재요청 부탁드립니다. ");


        const email = value;

        if (!validateEmail(value)) {
            alert("올바른 이메일 주소를 입력해주세요.");

            const emailInput = document.getElementById("email");
            emailInput.value = "";
            emailInput.focus();

            return;
        }

        const response = getEmailAuthCode(email);

        response.then((response) => {

            setIsProcessing(true);
            setShowTimer(true);

        });
    };

    /*
     * 이메일 인증코드 확인 핸들러
     */

    const onAuthCodeVerificationHandler = () => {
        if (authCode.length !== 6 || typeof authCode !== "string") {
            alert("인증번호는 6자리 문자열이어야 합니다.");
            return;
        }

        const data = {
            code: authCode,
            email: value,
        };
        const authTokenResponse = verifyAuthCodeAndRequestAuthToken(data);

        authTokenResponse
            .then((res) => {

                setAuthCodeMessage("인증이 완료되었습니다.");
                setIsAvailable(true);

                const token = res.data.data.authentication;

                const emailData = {
                    email: data.email
                }

                const deleteData = {
                    'email': value,
                }

                const deleteResponse = deleteAuthCode(deleteData);

                deleteResponse
                    .then((res) => {

                        setEmail(value);

                        navigate('/signup/id');

                        return;
                    })
                    .catch((error) => {
                        console.log(error);
                        resetState();

                        alert(
                            "인증 확인 중 예상치 못한 에러가 발생했습니다. 재 인증 부탁드립니다."
                        );
                        return;
                    });

                if (!token || token.trim() === '') {
                    alert('접근 가능 권한이 없습니다. 지속적으로 같은 오류 발생 시 자사에 문의 부탁드립니다.');

                    navigate("/");

                    return;
                }

                localStorage.setItem('authToken', token);


            })
            .catch((error) => {
                console.log(error);
                setAuthCodeError(error.message);

                alert(authCodeError);
                setIsAvailable(false);
            });
    };




    const onAuthCodeChange = (e) => {
        setAuthCode(e.target.value);
    };

    // 이메일 입력 핸들러를 정의합니다.
    const onChangeEmailHandler = (e) => {
        const value = e.target.value;

        // 이메일 입력이 없을 경우, 에러와 메시지를 초기화합니다.
        if (value === "") {
            setError("");
            setMessage("");
        } else {
            // 이메일 중복 및 유효성 검사를 수행합니다.
            checkEmailDuplication(value);
        }

        setValue(value);
    };

    // 이메일 유효성 검사 함수를 정의합니다.
    const validateEmail = (email) => {
        const emailRegex = /^[a-zA-Z0-9._%+-]+@(?:naver\.com|gmail\.com)$/;
        return emailRegex.test(email);
    };

    // 이메일 중복 검사 함수를 정의합니다.    public String getUsername(String accessToken) {
    //         return Jwts.parser()
    //                 .setSigningKey(key)
    //                 .deserializeJsonWith(deserializer)
    //                 .build()
    //                 .parseClaimsJws(accessToken)
    //                 .getBody()
    //                 .getSubject();
    //
    const checkEmailDuplication = async (email) => {
        await axiosInstance
            .post("/api/v1/auths/email/duplicates", {
                email: email,
            })
            .then((response) => {
                if (!response || !response.data || response.data.isDuplicated == null || response.data.message == null) {
                    setError("잘못된 요청입니다.");
                    return;
                }

                setError("");
                setMessage(response.data.message);

                if (!response.data.isDuplicated) {
                    setIsEmailAvailable(true);
                }
            })
            .catch(function(error) {
                setMessage("");

                if (!error || !error.response || !error.response.data) {
                    setError("오류가 발생했습니다.");

                    setIsEmailAvailable(false);
                    return;
                }

                setError(error.response.data.message);

                setIsEmailAvailable(false);
            });
    };


    // useState 초기화
    const resetState = () => {
        setError("");
        setMessage("");
        setAuthCodeError("");
        setAuthCodeMessage("");
        setIsIdDuplicated(false);
        setIsEmailAvailable(false);
        setIsAvailable(false);
        setIsButtonAvailable(true);
        setIsProcessing(false);
        setValue("");
        setEmail("");
        setAuthCode("");
        setShowTimer(false);
    };

    // 에러와 메시지를 표시하는 컴포넌트를 정의합니다.
    const DisplayMessage = ({
        error,
        message
    }) => {

        if (error) {
            return < Style.ErrorMessage > {
                error
            } < /Style.ErrorMessage>;
        } else if (message) {
            return < Style.Message > {
                message
            } < /Style.Message>;
        }
        return null;
    };

  // RegistrationForm 컴포넌트의 렌더링 내용을 정의합니다.
  return ( <
    Style.MainContainer >
    <
    Style.Section >
    <
    Style.SectionHeader >
    <
    Style.Icon loading = "lazy"
    src = "https://cdn.builder.io/api/v1/image/assets/TEMP/cb443ed5e5fad0a5a243fa3140cdf82a96fd899f2e94e9517abfeabceaf9429a?apiKey=a65641faa3d54339891445c030384eb2&"
    alt = "Sign Up" /
    >
    <
    Style.Title > 회원가입 < /Style.Title> <
    /Style.SectionHeader> <
    Style.Description >
    가입 절차를 위해 아래 절차 동의를 눌러주시고 본인인증을 진행해주세요. <
    /Style.Description> <
    /Style.Section> <
    Style.FormSection >
    <
    Style.Instruction >
    <
    Style.InstructionTitle > 이메일 인증 < /Style.InstructionTitle>

    <
    Style.Input onChange = {
      onChangeEmailHandler
    }
    type = "email"
    id = "email"
    name = "email"
    value = {
      value
    }
    placeholder = "이메일 입력(naver , google)"
    theme = "underLine"
    maxLength = {
      30
    }
    /> <
    /Style.Instruction>

    <
    DisplayMessage error = {
      error
    }
    message = {
      message
    }
    />

    <
    Style.Button type = "submit"
    disabled = {
      !isEmailAvailable
    }
    onClick = {
      onEmailCodeRequest
    } >
    번호 전송 <
    /Style.Button>

    <
    Style.VerificationTitle > 인증번호 입력 < /Style.VerificationTitle>

    <
    Style.Input type = "text"
    id = "authCode"
    name = "authCode"
    value = {
      authCode
    }
    onChange = {
      onAuthCodeChange
    }
    placeholder = "해당 이메일로 발송된 인증번호 6자리를 눌러주세요."
    theme = "underLine"
    maxLength = {
      6
    }
    />

    <
    DisplayMessage error = {
      authCodeError
    }
    message = {
      authCodeMessage
    }
    />

    {
      showTimer && ( <
        Timer initialSeconds = {
          180
        }
        onTimerEnd = {
          () => {
            resetState();

            const data = {
              email: value,
            };
            console.log(data);
            const result = deleteAuthCode(data);

            result
              .then((response) => {
                console.log(response);
                alert("인증 시간이 초과했습니다. 다시 인증 부탁드립니다. ");
              })
              .catch((err) => {
                console.log(err);
                alert(
                  "인증 요청 중 오류가 발생했습니다. 다시 인증 부탁드립니다. "
                );
              });
          }
        }
        />
      )
    }

    <Style.VerifyButton type = "submit"
    disabled = {!isAvailable}
    disabled = {authCode.length !== 6}
    onClick = {
      onAuthCodeVerificationHandler
    } >인증 확인

    </Style.VerifyButton>

    </Style.FormSection>

    <Style.ProgressIndicator/>
    </Style.MainContainer>
  )
};
export default RegistrationForm;