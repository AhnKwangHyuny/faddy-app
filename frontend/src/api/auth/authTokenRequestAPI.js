import {axiosInstance} from '../axiosInstance.js';
import { END_POINTS } from '../../constants/api';

export const verifyAuthCodeAndRequestAuthToken = async(data) => {

    if (!data || data.code == null || data.email == null) {
        alert("인증번호 및 이메일을 정확히 입력바랍니다.");
        return;
    }

    const response = await axiosInstance.post(END_POINTS.VERIFY_AUTH_CODE , data);

    return response;
}


// 서버에 임시 저장된 authCode 삭제를 요청한다.
export const deleteAuthCode = async(data) => {

  if (!data ||  data.email == null) {
        console.warn("인증 코드 삭제 중 문제가 발생했습니다. ");
        return;
      }

  const response = await axiosInstance.delete(END_POINTS.DELETE_AUTH_CODE , {data});

  return response;
}