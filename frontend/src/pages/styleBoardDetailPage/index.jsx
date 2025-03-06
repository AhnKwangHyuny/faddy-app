import { useParams, useSearchParams, useNavigate } from 'react-router-dom';
import { useEffect, useState, useCallback } from 'react';
import { mapTopicToCategory } from "pages/styleBoardCreatePage/data/mapTopicToCategory";
import { validateData } from "pages/styleBoardDetailPage/services/validation";
import { testData } from "pages/styleBoardDetailPage/data/testData";
import { formatCreatedAt } from "pages/styleBoardDetailPage/utils/formatDate";
import { mapTopicToHashTagName } from "pages/styleBoardDetailPage/data/mapTopicToHashTagName";

import Header from "pages/styleBoardDetailPage/Components/Header";
import TagList from "pages/styleBoardDetailPage/Components/TagList";
import MetaInfo from "pages/styleBoardDetailPage/Components/MetaInfo";
import ContentViewer from "pages/styleBoardDetailPage/Components/ContentViewer";
import InteractionBar from "pages/styleBoardDetailPage/Components/InteractionBar";
import ContentDivider from "shared/ui/ContentDivider";
import CommentSection from "pages/styleBoardDetailPage/Components/CommentSection";

import { checkIsOwner, getStyleBoardDetailData , getStyleBoardComments} from "pages/styleBoardDetailPage/api/get";
import { createStyleBoardComment, createStyleBoardReply } from "pages/styleBoardDetailPage/api/post";

const StyleBoardDetailPage = () => {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const { id } = useParams();
    const topic = searchParams.get('topic');

    const [styleBoardData, setStyleBoardData] = useState(null);
    const [isOwner, setIsOwner] = useState(false);
    const [loading, setLoading] = useState(true);
    const [comments, setComments] = useState([]);

    const category = mapTopicToCategory(topic).toLowerCase();
    const hashTagName = mapTopicToHashTagName(topic);

    const DATA_LOAD_ERROR_MESSAGE = "[error] 데이터를 불러오는데 실패했습니다.";

    useEffect(() => {
        // styleBoard Detail Data 불러오기
        const fetchData = async () => {
            try {
                const authentication = localStorage.getItem('ACCESS_TOKEN');

                if (!authentication) {
                    setIsOwner(false);
                } else {
                    const ownerResponse = await checkIsOwner(id);
                    setIsOwner(ownerResponse.isOwner);
                }

                const response = await getStyleBoardDetailData(id, category);

                const userProfile = {
                    nickname: response.nickname,
                    level: response.userLevel,
                    profileImageUrl: response.profileImageUrl,
                    createdAt: formatCreatedAt(response.createdAt),
                };

                const interaction = {
                    views: response.viewCount,
                    likes: response.likeCount,
                };

                setStyleBoardData({ ...response, userProfile ,interaction });

            } catch (error) {
                console.error(DATA_LOAD_ERROR_MESSAGE, error);
                alert(DATA_LOAD_ERROR_MESSAGE);
                navigate(-1);
            } finally {
                setLoading(false);
            }
        };

        // styleBoard Comment Data 불러오기
        const fetchComments = async () => {
            try {
                const response = await getStyleBoardComments(id);
                setComments(response);
            } catch (error) {
                console.error(DATA_LOAD_ERROR_MESSAGE, error);
                alert(DATA_LOAD_ERROR_MESSAGE);
                navigate(-1);
            }
        };

        fetchData();
        fetchComments();
    }, [id, category, navigate]);

    const handleBackButtonClick = () => {
        navigate(-1);
    };

    const handleOptionButtonClick = () => {
        console.log("옵션 버튼 클릭됨");
    };

    const handleAddComment = useCallback(async (comment, parentId = null) => {
        try {
            if (parentId) {
                const response = await createStyleBoardReply(id, parentId, comment);

                setComments((prevComments) =>
                    prevComments.map((c) =>
                        c.id === parentId
                            ? { ...c, replies: [...(c.replies || []), response] }
                            : c
                    )
                );
            } else {
                const response = await createStyleBoardComment(id, comment);
                setComments((prevComments) => [ ...prevComments , response]);
            }
        } catch (error) {
            console.error('댓글 추가에 실패했습니다.', error);
        }
    }, [id]);

    if (loading) {
        return <div>로딩 중...</div>;
    }

    return (
        <section style={{ minHeight: '1300px', marginBottom: '60px', position: 'relative' }}>
            {styleBoardData && (
                <>
                    <Header
                        onBackButtonClick={handleBackButtonClick}
                        onOptionButtonClick={handleOptionButtonClick}
                        isOwner={isOwner}
                    />
                    <TagList topic={hashTagName} tags={styleBoardData.hashTags} />
                    <MetaInfo userProfile={styleBoardData.userProfile} interaction={styleBoardData.interaction} />
                    <ContentViewer title={styleBoardData.title} content={styleBoardData.content} />
                    <InteractionBar likeCount={styleBoardData.likeCount} liked = {styleBoardData.liked} shares={testData.interaction.shares} />
                    <ContentDivider />
                    <CommentSection comments={comments} onAddComment={handleAddComment} />
                </>
            )}
        </section>
    );
}

export default StyleBoardDetailPage;
