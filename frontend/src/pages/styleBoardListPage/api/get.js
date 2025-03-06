import { END_POINTS } from 'constants/api';
import { userRequestInstance } from 'api/axiosInstance';


export const getStyleBoards = async (filterOptions = {}) => {
    const body = {
        sort: 'newest',
        page: 0,
        size: 4,
        ...filterOptions
    };

    try {
        const response = await userRequestInstance.post(END_POINTS.GET_STYLE_BOARDS, body);
        return response.data.data;
    } catch (error) {
        console.error('Error fetching style boards:', error);
        throw error;
    }
};

