import { END_POINTS ,   AXIOS_BASE_URL } from 'constants/api';
import {userRequestInstance} from 'api/axiosInstance';

export const postUserLogin = async({username, password}) => {

    const data = {
            username: username,
            password: password
        };


    return await userRequestInstance.post(END_POINTS.LOGIN , data);
}

export const saveImages = async (files) => {

    return await userRequestInstance.post(END_POINTS.UPLOAD_IMAGES , files , {
        headers: {
            'content-Type': 'multipart/form-data',
        },
    });
}

export const saveImage = async (formData , category) => {
    return await userRequestInstance.post(END_POINTS.UPLOAD_IMAGE(category) , formData , {
        headers: {
            'content-Type': 'multipart/form-data',
        },
    });
}

export const uploadSnap =  async (snapData) => {

    return await userRequestInstance.post(END_POINTS.POST_SNAP , snapData );
}

export const reissueToken = (refreshToken) => {
    const config = {
        headers: {
            'Authorization': `Bearer ${refreshToken}`,
        }
    }

    return userRequestInstance.post(END_POINTS.REISSUE_TOKEN , config);
}

/** 좋아요 요청 */
export const postLike = async (snapId) => {
    try {
        const response = await userRequestInstance.post(END_POINTS.LIKE_CLICK(snapId));
        return response.data;
    } catch (error) {
        console.error('좋아요 요청 실패', error);
        throw error;
    }
};

/** 팔로우 요청 */
export const follow = async(toUsername) => {
    try {
        const data = {
            toUsername,
        }

        const response = await userRequestInstance.post(END_POINTS.FOLLOW , data );

        return response.status === 200;

    } catch (error) {
        console.error(error.data.message);
        return false;
    }
}

/** 채팅방 생성 api*/
export const createFriendChatRoom = async (request) => {
    try {

        const response = await userRequestInstance.post(END_POINTS.CREATE_CHAT_ROOM , request);

        return response.data;

    } catch (error) {
        console.error(error);
        return error;
    }
}

