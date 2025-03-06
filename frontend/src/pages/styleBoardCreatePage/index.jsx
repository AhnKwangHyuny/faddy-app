import React, { useState, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { isValidTopic } from 'pages/styleBoardCreatePage/data/validTopics';
import { mapTopicToTitle } from 'pages/styleBoardCreatePage/data/mapTopicToTitle';
import Header from "pages/styleBoardCreatePage/components/Header";
import TitleInput from 'pages/styleBoardCreatePage/components/TitleInput';
import ContentEditor from 'pages/styleBoardCreatePage/components/ContentEditor';
import HashTagBox from 'pages/Snap/Components/HashTagBox/HashTagBox';
import createStyleBoard from 'pages/styleBoardCreatePage/api/createStyleBoard';
import {validateStyleBoardData} from 'pages/styleBoardCreatePage/services/validation';
import {mapTopicToCategory} from 'pages/styleBoardCreatePage/data/mapTopicToCategory';

const StyleBoardCreatePage = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const topicValue = searchParams.get('topic');
    const [tags , setTags] = useState([]);
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');

    const [errors, setErrors] = useState({});

    useEffect(() => {
        if (!isValidTopic(topicValue)) {
            alert('없는 주제입니다.');
            navigate(-1);
        }
    }, [topicValue, navigate]);

    // 서버에 저장 요청
    const onSubmit = async () => {
        const category = mapTopicToCategory(topicValue);

        if (!category) {
            alert('없는 주제입니다.');
            navigate(-1);
        }

        const requestData = {
            category: category,
            title: title,
            content: content,
            hashTags: tags.map((tag) => ({ name: tag })),
        }
        const validationErrors = validateStyleBoardData(requestData); // 데이터 검증

        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            alert(Object.values(validationErrors).join('\n'));
            return;
        }

        try {
            const result = await createStyleBoard(requestData);
            if(result.status == 201) {
                alert('스타일 보드가 성공적으로 생성되었습니다.');
                navigate('/styleBoards'); // 성공 시 리다이렉트 (추후 detail로 변경)
            }

        } catch (error) {
            alert('스타일 보드 생성 중 오류가 발생했습니다.');
        }
    };

    const mappedTopic = mapTopicToTitle(topicValue);

    return (
        <section style={{ minHeight: '700px' }}>
            <Header topic={mappedTopic} onSubmit={onSubmit} />
            <TitleInput title={title} setTitle={setTitle} />
            <ContentEditor content={content} setContent={setContent} />
            <HashTagBox labelHide = {false} tags={tags} setTags={setTags} />
        </section>
    );
};

export default StyleBoardCreatePage;
