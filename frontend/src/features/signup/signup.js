import { axiosInstance } from 'api/axiosInstance';

export async function signup(fields) {

  const { username, password, nickname, email } = fields;

  try {
    const response = await axiosInstance.post('/api/v1/users', {
      username,
      password,
      nickname,
      email,
    });

    // 회원가입 성공 처리

    return response;
  } catch (error) {

    console.warn(error);

    throw error;
  }
}
