import { END_POINTS } from 'constants/api';
import { userRequestInstance } from 'api/axiosInstance';


export const checkIsOwner = async (styleBoardId) => {
    try {
        const response = await userRequestInstance.get(END_POINTS.CHECK_IS_STYLE_BOARD_OWNER(styleBoardId));
        return response.data.data;
    } catch (error) {
        console.error('Error fetching style board detail:', error);
        throw error;
    }
};

export const getStyleBoardDetailData = async (styleBoardId, category) => {
    try {
        const response = await userRequestInstance.get(END_POINTS.GET_STYLE_BOARD_DETAIL(styleBoardId, category));
        return response.data.data;
    } catch (error) {
        console.error('Error fetching style board detail:', error);
        throw error;
    }
};

export const getStyleBoardComments = async (styleBoardId) => {
    try {
        const response = await userRequestInstance.get(END_POINTS.GET_STYLE_BOARD_COMMENTS(styleBoardId));
        console.log(response);
        return response.data.data;
    } catch (error) {
        console.error('Error fetching style board comments:', error);
        throw error;
    }
}



