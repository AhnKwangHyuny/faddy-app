import {axiosInstance} from '../axiosInstance.js';
import { END_POINTS } from '../../constants/api';

export const getEmailAuthCode = async(email) => {

    const data = {
      email : email,
    }

    const response = await axiosInstance.post(END_POINTS.GET_EMAIL_AUTH_CODE , data);

    // 응답 헤더 파싱
    const headers = response.headers;


    console.log(response);


    return response;
}