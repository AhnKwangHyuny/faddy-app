import { END_POINTS } from 'constants/api';
import { userRequestInstance } from 'api/axiosInstance';

export const checkIsOwner = async (styleBoardId) => {
    try {
        const response = await userRequestInstance.get(END_POINTS.CHECK_IS_STYLE_BOARD_OWNER(styleBoardId));

        if (response.data.status === 200) {
            return response.data.data.isOwner;
        } else {
            return false;
        }
    } catch (error) {
        console.log(error);
        if (error.response && error.response.status === 401) {
            return error.response.data.isOwner;
        }
        throw error;
    }
};

export const getStyleBoardEditDataById = async (styleBoardId) => {
    try {
        const response = await userRequestInstance.get(END_POINTS.GET_STYLE_BOARD_EDIT_DATA(styleBoardId));
        return response.data.data;
    } catch (error) {
        console.error('Error fetching style board detail:', error);
        throw error;
    }
};