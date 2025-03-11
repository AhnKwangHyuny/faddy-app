import React, {useEffect, useState} from 'react';
import CommentCard from "widgets/Comment/CommentCard/CommentCard";
import getLevelData from "../../../utils/user/getLevelData";

const CommentModal = ({showModal ,setShowModal}) => {
    const [comment, setComment] = useState('');

    const handleCLose = () => {
        setShowModal(false);
    };


    const comments = [
        {
            id: 1,
            user: {
                level : "Lv4",
                imageUrl: "/default_profile.jpg",
                username: '사용자1',
            },
            content: '멋진 사진이네요! 배경도 정말 예뻐요.',
            createdAt: '19시간 전',
        },
        {
            id: 2,
            user: {
                level : "Lv3",
                imageUrl: "/default_profile.jpg",
                username: '사용자2',
            },
            content: '옷도 잘 어울려요!',
            createdAt: '20시간 전',
        },
        {
            id: 3,
            user: {
                level : "Lv2",
                imageUrl: "/default_profile.jpg",
                username: '사용자3',
            },
            content: '저도 이런 사진 찍어보고 싶어요!',
            createdAt: '21시간 전',
        },
        {
            id: 4,
            user: {
                level : "Lv5",
                imageUrl: "/default_profile.jpg",
                username: '사용자2',
            },
            content: '저도 이런 사진 찍어보고 싶어요!',
            createdAt: '1일 전',
        },
        {
            id: 5,
            user: {
                level : "Lv4",
                imageUrl: "/default_profile.jpg",
                username: '사용자5',
            },
            content: '하쿠나 마타타네',
            createdAt: '13일 전',
        },

    ];

    const handleCommentChange = (e) => {
        setComment(e.target.value);
    };

    const handleSubmit = () => {
        // 댓글 제출 로직 추가
    };

    return (
        <div className={`comment-modal ${showModal ? 'open' : ''}`}>

            <div className="comment-modal__wrapper">
                <div className="comment-modal__header">
                    <div className="close-button" onClick={handleCLose}>
                       <span className="material-icons">
                            close
                        </span>
                    </div>

                    <div className="comment-count__container">
                        <span className="comment-count">
                            댓글 42
                        </span>
                    </div>
                </div>


                <div className="comment-modal__content">
                    <ul className="comment-list">
                        {comments.map((comment) => (
                            <li className="comment-wrapper" key={comment.id}>
                                <CommentCard comment={comment}/>
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default CommentModal;