import { END_POINTS } from 'constants/api';
import { userRequestInstance } from 'api/axiosInstance';

export const createStyleBoardComment = async (styleBoardId, content) => {
    try {
        const data = {
            content,
        }

        const response = await userRequestInstance.post(END_POINTS.CREATE_STYLE_BOARD_COMMENT(styleBoardId), data);
        console.log('response:', response);
        return response.data.data;
    } catch (error) {
        console.error('Error creating style board comment:', error);
        throw error;
    }
}

export const createStyleBoardReply = async (styleBoardId, parentId, content) => {
    try {
        const data = {
            content,
        }

        const response = await userRequestInstance.post(END_POINTS.CREATE_STYLE_BOARD_COMMENT_REPLY(styleBoardId, parentId), data);
        return response.data.data;
    } catch (error) {
        console.error('Error creating style board reply:', error);
        throw error;
    }
}

export const createCommentLike = async (objectType , objectId) => {
    try {
        const response = await userRequestInstance.post(END_POINTS.CREATE_LIKE(objectType ,objectId ));
        return response.data.status;
    } catch (error) {
        console.error('Error adding like:', error);
        return error.response.data.status;
    }
}