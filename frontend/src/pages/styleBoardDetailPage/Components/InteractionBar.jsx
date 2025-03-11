import React, { useState } from 'react';
import { mapToComment } from "pages/styleBoardDetailPage/utils/responseMapper";
import { createCommentLike } from "pages/styleBoardDetailPage/api/post";
import { cancelLike } from "pages/styleBoardDetailPage/api/delete";
import {mapContentTypeToLabel} from "data/contentType";
import { useParams, useSearchParams, useNavigate } from 'react-router-dom';


const InteractionBar = ({ likeCount , liked , shares }) => {

    const [likes, setLikes] = useState(likeCount);
    const [isLiked, setIsLiked] = useState(liked);
    const contentType = mapContentTypeToLabel("styleBoard");

    const [searchParams] = useSearchParams();
    const { id } = useParams();

    /**
    like 버튼 클릭 시 좋아요 추가/취소 핸들링
    */
    const handleLikeClick = async () => {

       //비로그인 유저 like 금지
       const authentication = localStorage.getItem('ACCESS_TOKEN');

        if (!authentication) {
            alert("로그인이 필요한 서비스입니다.");
            return;
        }

       try {
           console.log(isLiked);
           if (isLiked) {
               const resultStatus = await cancelLike(contentType,id);
               console.log(resultStatus);
               if (resultStatus !== 200) {
                   alert("[error] 좋아요 클릭 이벤트 수행을 실패했습니다.")
                   return;
               }
           } else {
               const resultStatus = await createCommentLike(contentType, id);

               if(resultStatus !== 201) {
                   alert("[error] 좋아요 클릭 이벤트 수행을 실패했습니다.")
                   return;
               }
           }

           setIsLiked(!isLiked);
           setLikes(isLiked ? likes - 1 : likes + 1);

       } catch (error) {
           console.error('Error updating like:', error);
           setIsLiked(isLiked); // 원래 상태로 되돌림
           setLikes(isLiked ? likes + 1 : likes - 1); // 원래 카운트로 되돌림
       }
    };

    return (
        <div className="interaction-bar">
            <div className="interaction-bar__item like" onClick={handleLikeClick}>
                <span className={`material-icons thumb-up ${isLiked ? 'liked' : ''}`}>thumb_up</span>
                <span className="interaction-bar__count">{likes > 0 && likes }</span>
            </div>
            <div className="interaction-bar__item share">
                <span className="material-icons share-icon">share</span>
                <span className = "name">공유하기</span>
            </div>
        </div>
    );
};

export default InteractionBar;
