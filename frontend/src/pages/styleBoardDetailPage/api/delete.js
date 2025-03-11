import { END_POINTS } from 'constants/api';
import { userRequestInstance } from 'api/axiosInstance';

export const cancelLike = async (objectType , objectId) => {
    try {
        const response = await userRequestInstance.delete(END_POINTS.CANCEL_LIKE(objectType ,objectId ));
        return response.data.status;
    } catch (error) {
        console.error('Error canceling like:', error);
        throw error.response.data.status;
    }
}

export const deleteStyleBoard = async (styleBoardId) => {
    try {
        const response = await userRequestInstance.delete(END_POINTS.DELETE_STYLE_BOARD(styleBoardId));
        console.log(response.data);
        return response.data;
    } catch (error) {
        console.error('Error deleting style board:', error);
        throw error;
    }
}