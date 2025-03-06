import React, {useState, useEffect, useContext} from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHeart as fasHeart } from '@fortawesome/free-solid-svg-icons';
import { faHeart } from '@fortawesome/free-regular-svg-icons';
import SnapIdContext from "shared/context/SnapIdContext";
import {getLikeCount} from "api/get";

const LikeButton = ({onLikeChange, showLikeCount = true }) => {
    const [animationClass, setAnimationClass] = useState('');
    const [isLiked , setIsLiked] = useState(false);
    const [likeCount , setLikeCount] = useState(null);
    const snapId = useContext(SnapIdContext);
    const handleLikeClick = () => {

        // 서버로 like 업데이트 요청 api
        onLikeChange();
        setIsLiked(!isLiked);
        //Like click animation
        // setAnimationClass('animate');
    };

   useEffect(()=> {

       const fetchLikeCount = async () => { // 함수 이름을 변경했습니다.
           if(showLikeCount && snapId != null) {
               try {
                   const count = await getLikeCount(snapId); // api/get에서 가져온 함수를 호출합니다.
                   setLikeCount( Number(count));
               } catch (error) {
                   console.error("Like count를 불러오는 데 실패했습니다.", error);
               }
           }
       };

       fetchLikeCount();

   }, []);

    return (
        <div className="like">
            <div className={`like-button ${isLiked ? 'liked' : ''}`} onClick={handleLikeClick}>
                <FontAwesomeIcon icon={isLiked ? fasHeart : faHeart} className={`heart-icon ${animationClass}`} />
            </div>
            {showLikeCount && likeCount && (
                <div className="like-count">
                    <span className="count">{isLiked ? likeCount + 1 : likeCount - 1}</span>
                </div>
            )}
        </div>
    );
};

export default LikeButton;