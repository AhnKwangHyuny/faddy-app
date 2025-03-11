import { END_POINTS } from 'constants/api';
import { userRequestInstance } from 'api/axiosInstance';

export const updateStyleBoardDataById = async (styleBoardId, data) => {
    try {
        const response = await userRequestInstance.put(END_POINTS.UPDATE_STYLE_BOARD_DATA(styleBoardId), data);
        return response.data;
    } catch (error) {
        console.error('Error updating style board data:', error);
        throw error;
    }
};