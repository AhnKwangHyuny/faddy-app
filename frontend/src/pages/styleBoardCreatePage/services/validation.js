import {isValidTopic} from 'pages/styleBoardCreatePage/data/validTopics';

export const validateStyleBoardData = ({ category, title, content }) => {
    const errors = {};

    if (!category) {
        errors.category = '주제는 필수 항목입니다.';
    }

    if (!title || title.trim() === '') {
        errors.title = '제목은 필수 항목입니다.';
    }

    if (!content || content.trim() === '') {
        errors.content = '내용은 필수 항목입니다.';
    }

    return errors;
};