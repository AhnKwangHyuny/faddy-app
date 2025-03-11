// 토큰 갱신 요청 (refresh-token)
import {userRequestInstance} from "./axiosInstance";
import {END_POINTS} from "../constants/api";


export const getUserId = async(username) => {
    const config = {
        params: {
            username,
        }
    }

    return await userRequestInstance.get(END_POINTS.GET_USER_ID , config );

};

export const checkUserIdExists = async (userId) => {

    const config = {
        headers: {
            'Content-Type': 'application/json'
        }
    }

    const requestBody = JSON.stringify({ userId });

    return await userRequestInstance.post(END_POINTS.CHECK_USER_ID_EXISTS,requestBody , config);

};



export const verifyToken = (accessToken) => {
    const config = {
        headers : {
            'Authentication': `Bearer ${accessToken}`,
        }
    }

    return userRequestInstance.get(END_POINTS.VERIFY_TOKEN , config);
}

/**스냅 데이터 가져오기*/
export const getSnapData = async (snapId) => {

    const config = {
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            snapId: snapId
        }
    }

    return await userRequestInstance.get(END_POINTS.SNAP_DETAIL, config);
};

/**좋아요 수 조회*/
export const getLikeCount = async (snapId) => {
    try {
        const response = await userRequestInstance.get(END_POINTS.GET_LIKE_COUNT(snapId));
        return response.data.data;
    } catch (error) {
        console.error('좋아요 수 조회 요청 실패', error);
        throw error;
    }
};

/**paging loader */
export const getMoreThumbnails = async (page) => {
    try {
        const response = await userRequestInstance.get(END_POINTS.MORE_THUMBNAILS(page));
        const data = await response.data.data;
        console.log(data);

        return data;

    } catch (error) {
        if (error.response) {
          console.error(`Error ${error.response.status}: ${error.response.data.message}`);
          console.error(`Error Code: ${error.response.data.code}`);
        } else {
          console.error('Error: ', error.message);
        }
        throw error;
      }
}

/** follow 상태 확인*/
export const checkFollowStatus = async (toUsername) => {
  try {
    const response = await userRequestInstance.get(END_POINTS.CHECK_FOLLOW(toUsername));
    return response.status === 200 && response.data.data.isFollowed === true;

  } catch (error) {
    console.warn(error);
    return false;
  }
};

/** follow한 유저와 follow준 유저 모두 가져오기*/
export const getFollowerListByFollowingId = async () => {
  try {
    const userId = localStorage.getItem('userId');
    // userId is null 이면 error 발생 시키기
    if (!userId) {
      // error code 403
      throw new Error('userId is null');
    }

    const response = await userRequestInstance.get(END_POINTS.GET_FOLLOWERS(userId));

    return response.data.data;
  } catch (error) {

    console.error('팔로우 리스트 조회 실패', error);
    // return error response
    throw error;
  }
};

export const getTalkList = async (type, page) => {
    try {
        const response = await userRequestInstance.get(END_POINTS.GET_ALL_TALKS(type, page));
        console.log(page);
        return response.data.data;
    } catch (error) {
        console.error('채팅 리스트를 조회하는데 실패했습니다.', error);
        throw error;
    }
};


export const fetchChatMessages = async (chatId) => {
    try {
        const response = await userRequestInstance.get(END_POINTS.GET_CHATS(chatId));
        return response.data.data;
    } catch (error) {
        console.error('채팅 메시지를 조회하는데 실패했습니다.', error);
        throw error;
    }
}

// 채팅방 사용자 접근권한 확인 요청 API
export const checkChatRoomAccess = async (roomId) => {
    try {
        const response = await userRequestInstance.get(END_POINTS.CHECK_CHAT_ROOM_ACCESS(roomId));
        return response.status === 200;

    } catch (error) {
        console.error('채팅방 접근 권한을 확인하는데 실패했습니다.', error);
        throw error;
    }
}

/**
 * 특정 채팅방에서 단일 유저 프로필을 조회
 * @param {string} roomId 채팅방 ID
 * @param {string} username 유저 이름
 * @returns {Promise<object>} 유저 프로필
 */

export const fetchChatRoomUserProfile = async (roomId, username) => {
    try {
        const response = await userRequestInstance.get(END_POINTS.GET_PROFILE_IN_CHATROOM(roomId, username));
        const userProfile = response.data.data;

        // {username : {userProfileInfo}} 형태로 변환
        const transformedProfile = {
                    [userProfile.username]: {
                        nickname: userProfile.nickname,
                        profileImageUrl: userProfile.profileImageUrl,
                        level: userProfile.level,
                    }
                };

        return transformedProfile;

    } catch (error) {
        console.error('사용자 프로필을 가져오는데 실패했습니다.', error);
        throw error;
    }
};

/**
 * 특정 채팅방의 모든 유저 프로필을 조회
 * @param {string} roomId 채팅방 ID
 * @returns {Json<UserProfile>} 유저 프로필 목록
 */
export const fetchChatRoomUserProfiles = async (roomId) => {
    try {
        const response = await userRequestInstance.get(END_POINTS.GET_ALL_PROFILES_IN_CHATROOM(roomId));
        const userProfiles = response.data.data;

        const transformedProfiles = userProfiles.reduce((acc, { userProfileDTO }) => {
            acc[userProfileDTO.username] = {
                nickname: userProfileDTO.nickname,
                profileImageUrl: userProfileDTO.profileImageUrl,
                level: userProfileDTO.level,
            };
            return acc;
        }, {});

        return transformedProfiles;
    } catch (error) {
        console.error('사용자 프로필을 가져오는데 실패했습니다.', error);
        throw error;
    }
};

