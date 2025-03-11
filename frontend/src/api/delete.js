import { END_POINTS ,   AXIOS_BASE_URL } from 'constants/api';
import {userRequestInstance} from 'api/axiosInstance';

/**
 *  image 삭제 request
 * */
export const deleteImage = async(request) => {

    const data = {
        hashedName : request.hashedName,
        url : request.url,
    };

    const config = {
        data: data
    };

    return await userRequestInstance.delete(END_POINTS.DELETE_IMAGE , config);
}

/**
 *  복수의 image 삭제 요청
 * */

export const deleteImages = async (requests) => {
   if (requests.length === 0) {
       return;
   }

   requests.map(request => {
       if (!request.hashedName || !request.url) {
           throw new Error('hashedName 또는 url이 없습니다.');
       }
   });

   const imageList = requests.map(request => ({
       hashedName: request.hashedName,
       url: request.url
   }));

   const config = {
       data: imageList,
   };

   return await userRequestInstance.delete(END_POINTS.DELETE_IMAGES, config);
};

/** 좋아요 취소 요청*/
export const deleteLike = async (snapId) => {
    try {
        const response = await userRequestInstance.delete(END_POINTS.UNLIKE_CLICK(snapId));
        return response.data;
    } catch (error) {
        console.error('좋아요 취소 요청 실패', error);
        throw error;
    }
};

/**언팔로우 요청*/
export const unFollow = async(toUsername) => {
     try {

            const response = await userRequestInstance.delete(END_POINTS.UNFOLLOW(toUsername));

            return response.status === 200;

        } catch (error) {
            console.error(error.data.message);
            return false;
        }
}

/**
 * 특정 채팅방에서 단일 유저 프로필을 삭제
 * @param {string} roomId 채팅방 ID
 * @param {string} username 유저 이름
 * @returns {Promise<void>}
 */
export const deleteUserProfile = async (roomId, username) => {
    try {
        await userRequestInstance.delete(END_POINTS.DELETE_PROFILE_IN_CHATROOM(roomId, username));
    } catch (error) {
        console.error('사용자 프로필을 삭제하는데 실패했습니다.', error);
        throw error;
    }
};

/**
 * 특정 채팅방의 모든 유저 프로필을 삭제
 * @param {string} roomId 채팅방 ID
 * @returns {Promise<void>}
 */
export const deleteAllUserProfiles = async (roomId) => {
    try {
        await userRequestInstance.delete(END_POINTS.DELETE_ALL_PROFILES_IN_CHATROOM(roomId));
    } catch (error) {
        console.error('모든 사용자 프로필을 삭제하는데 실패했습니다.', error);
        throw error;
    }
};