import {axiosInstance} from '../axiosInstance.js';
import { END_POINTS } from '../../constants/api';
import {ValidateUserId , validateNickname} from 'utils/Validate';



export const checkUserId = async (userId) => {

    if(userId === null || userId === undefined || !ValidateUserId(userId)) {
        console.warn("유저 아이디가 유효하지 않습니다.");
        return;
    }

    const data = {
        'userId': userId
    };

    const response = await axiosInstance.post(END_POINTS.CHECK_USER_ID , data);

    return response;
}

export const getNicknameValidationResponse = async (nickname) => {

        const data = {
            'nickname': nickname
        };

        const response = await axiosInstance.post(END_POINTS.CHECK_USER_NICKNAME , data);

    return response;
}
