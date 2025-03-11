import { axiosInstance } from 'api/axiosInstance';

import { END_POINTS } from 'constants/api';

export const checkEmailDuplication = async(email) => {
  const response = await axiosInstance.post('/api/v1/auths/email/duplicates' , {email});
  return response.data;

};