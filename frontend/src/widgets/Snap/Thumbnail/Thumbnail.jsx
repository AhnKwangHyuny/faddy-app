import React, { useState , useEffect } from 'react';
import {formatCount} from "utils/typeFormatUtils/typeFormatUtils";
import LikeButton from "widgets/LikeButton/LikeButton";
import {getLikeCount} from "api/get";
import { END_POINTS } from 'constants/api';
import {Link, useNavigate} from 'react-router-dom';




const loadLikeCount = async(snapId , setLikes) => {

    try {
        const likeCount = await getLikeCount(snapId);
        setLikes(likeCount);
    } catch (e) {
        console.error(e);

        // 0으로 초기화
        setLikes(0);
    }

};

const Thumbnail = ({data}) => {
    const [isLiked, setIsLiked] = useState(false);
    const [likes, setLikes] = useState(0);

    const handleLikeClick = () => {
        setIsLiked(!isLiked);
        setLikes(isLiked ? likes -1  : likes + 1);

        // like logic
    };

    useEffect(() => {
        loadLikeCount(data.snapId , setLikes);
    });

    return (
        <div className="thumbnail-card">
            <Link to={`${END_POINTS.SNAP_DETAIL_LOCATION}?snapId=${data.snapId}`}>
                <div className="image-container">
                    <img src={data.imageUrl} alt="Thumbnail" />

                    <div className="like-button__container">
                        <LikeButton
                            onLikeChange={handleLikeClick}
                            showLikeCount={false}
                        />
                    </div>
                </div>
                <div className="content">
                    <div className="user-profile">
                        <img src={data.profileImageUrl} alt="User Profile" />
                        <span>{data.nickname}</span>
                    </div>
                    {data.hashTags && data.hashTags.length > 0 && (
                        <div className="hashtags">
                            {data.hashTags.map((hashtag) => (
                                <span key={hashtag} className = "hashtag">#{hashtag}</span>
                            ))}
                        </div>
                    )}
                    <div className="stats">
                        <div className="view__container">
                            <span className={"material-icons view"}>
                                visibility
                            </span>

                            <span className="view-count">
                                    200
                            </span>
                        </div>

                        <div className="like__container">
                           <span
                               className={`material-icons like`}
                               style={{color: isLiked ? 'red' : 'inherit'}}
                           >
                                {isLiked ? 'favorite' : 'favorite_border'}
                            </span>

                            <span className="like-count">
                                {formatCount(likes)}
                            </span>
                        </div>
                    </div>
                </div>
            </Link>
        </div>
    );
};

export default Thumbnail;