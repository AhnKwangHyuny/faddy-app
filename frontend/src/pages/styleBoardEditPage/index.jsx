import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Header from 'pages/styleBoardEditPage/Components/Header';
import TitleEditor from 'pages/styleBoardEditPage/Components/TitleEditor';
import ContentEditor from 'pages/styleBoardEditPage/Components/ContentEditor';
import HashTagBox from 'pages/Snap/Components/HashTagBox/HashTagBox';
import { updateStyleBoardDataById } from 'pages/styleBoardEditPage/api/put';
import { getStyleBoardEditDataById , checkIsOwner } from 'pages/styleBoardEditPage/api/get';

const StyleBoardEditPage = () => {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [tags, setTags] = useState([]);
    const [isOwner, setIsOwner] = useState(false);
    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        const verifyOwnershipAndLoadData = async () => {
            try {
                const isOwner = await checkIsOwner(id);

                if (isOwner) {
                    setIsOwner(true);
                    const response = await getStyleBoardEditDataById(id);
                    setTitle(response.title);
                    setContent(response.content);
                    setTags(response.hashTags);
                } else {
                    alert("[error] 해당 게시글에 접근권한이 없습니다.");
                    navigate(-1);  // Redirect to the previous page
                }
            } catch (error) {
                console.error('Error verifying ownership or loading style board data:', error);
                alert("[error] 해당 게시글에 접근권한이 없습니다.");
                navigate(-1);  // Redirect to the previous page
            }
        };

        verifyOwnershipAndLoadData();
    }, [id, navigate]);

    const onUpdate = async () => {
        try {
            // 해시태그 목록을 HashTagDto 형식으로 변환
            const hashTags = tags.map(tag => ({ name: tag }));

            const response = await updateStyleBoardDataById(id, { title, content, hashTags });
            alert('업데이트가 성공적으로 진행되었습니다.');
        } catch (error) {
            console.error('Error updating style board data:', error);
            alert('Update failed');
        }

        navigate(`/styleBoards`);
    };

    const onSubmit = () => {
        console.log("submit");
    };

    return (
        <section>
            <Header onSubmit={onUpdate} />
            {isOwner && (
                <>
                    <TitleEditor title={title} setTitle={setTitle} />
                    <ContentEditor content={content} setContent={setContent} />
                    <HashTagBox labelHide={false} tags={tags} setTags={setTags} />
                </>
            )}
        </section>
    );
};

export default StyleBoardEditPage;
