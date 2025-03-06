import axios from 'axios';

import { AXIOS_BASE_URL, NETWORK } from 'constants/api';

export const axiosInstance = axios.create({
  baseURL: AXIOS_BASE_URL,
  withCredentials: true,
  useAuth: true,
});

export const userRequestInstance = axios.create({
  baseURL: AXIOS_BASE_URL,
});


// 클라이언트가 서버로 요청 시 토큰 담아서 요청
userRequestInstance.interceptors.request.use((config) => {


  setAccessToken(config); // header에 authorization 담기


  return config;

} , (error) => {
      return Promise.reject(error);
    }
);


// 초큰 유효성 검사
userRequestInstance.interceptors.response.use((response) => {
  return response;

}, async (error) => {
  try {
    const originalRequest = error.config;

    // error 객체 전체 로깅
    console.log(error);

    // 에러의 response 속성 여부 및 상태 코드 검사
    if (error.response && error.response.status === 403 && !originalRequest._retry) {
      originalRequest._retry = true; // 재시도 플래그 설정

      await refreshAccessToken();

      return userRequestInstance(originalRequest);
    }

    return Promise.reject(error);

  } catch (error) {

    console.error('요청 response 검증 중 에러가 발생했습니다.', error);
    return Promise.reject(error);
  }
});

// 유저 헤더에 쿠키 추가
export const setAccessToken = (config) => {
    const token = localStorage.getItem('ACCESS_TOKEN');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
};

// 토큰 갱신
const refreshAccessToken = async (refreshToken) => {

  const config = {
    "REFRESH_TOKEN" : refreshToken,
  }

  const response = await userRequestInstance.post("/api/v1/auths/reissue-token" , config);
  console.log(response.data);
  const { accessToken } = response.data; // 여기서 구조 분해 할당을 사용하여 accessToken 추출

  localStorage.setItem('ACCESS_TOKEN', accessToken);
}