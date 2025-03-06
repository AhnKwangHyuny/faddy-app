import { END_POINTS } from 'constants/api';
import { userRequestInstance } from 'api/axiosInstance';

const createStyleBoard = async (requestData) => {
    try {
        console.log(requestData);
        const response = await userRequestInstance.post(END_POINTS.CREATE_STYLE_BOARD, requestData);
        console.log(response);
        return response.data;
    } catch (error) {
        console.error('Error creating style board:', error);
        throw error;
    }
};

export default createStyleBoard;
