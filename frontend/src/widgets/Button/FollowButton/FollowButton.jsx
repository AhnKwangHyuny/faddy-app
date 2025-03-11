import React, { useState, useEffect} from 'react';
import { useNavigate } from "react-router-dom";
import { follow } from "api/post";
import {unFollow} from "api/delete";
import {checkFollowStatus} from "api/get";
import {useConfirm} from "shared/hooks/useConfirm";

export const FollowButtonByName = ({writerName }) => {
    const navigate = useNavigate();
    const username = localStorage.getItem("username");


    const isMine = username === writerName;
    const [isFollowing, setIsFollowing] = useState(false);
    const [isLoading, setIsLoading] = useState(false);

    const loginConfirm = () => navigate("/login");
    const cancelConfirm = () => {return};
    const confirm = useConfirm("로그인이 필요한 서비스입니다." , loginConfirm , cancelConfirm);

    // follow된 상대인지 확인
    useEffect(()=> {
        const handleCheckFollowStatus = async () => {
            const isFollowed = await checkFollowStatus(writerName);
            // follow가 안되어 있다면 초기 상태 유자
            if(!isFollowed) return;

            setIsFollowing(true);
        }

        handleCheckFollowStatus();
    } , []);


    const handleFollowButtonClick = async (username, shouldFollow) => {

        // 로그인된 사용자가 아닐 경우
        if(username === null) {
            // confirm modal
            confirm();
            return;
        }

        setIsLoading(true);
        try {
            const toUsername = username;
            const result = shouldFollow ? await follow(toUsername) : await unFollow(toUsername);

            if (!result) {
                alert(
                    shouldFollow ? "알 수 없는 에러로 친구 맺기 실패했습니다." : "친구 해제에 실패했습니다."
                );
            } else {
                setIsFollowing(shouldFollow);
            }
        } catch (error) {
            console.error(
                `${shouldFollow ? "팔로우" : "언팔로우"} 요청 중 에러가 발생했습니다.`,
                error
            );
        } finally {
            setIsLoading(false);
        }
    };

    const handleModifyButton = () => {
        // 추후 개발 예정
        console.log("hi");
    };

    return (
        <div className="follow-button">
            <div className="follow-button__wrapper">
                {isMine ? (
                    <button
                        className="button"
                        onClick={() => handleModifyButton()}
                        disabled={isLoading}
                    >
                        {isLoading ? (
                            <span className="loading-spinner"></span>
                        ) : isMine ? (
                            '수정하기'
                        ) : isFollowing ? (
                            '친구해제'
                        ) : (
                            '친구추가'
                        )}
                    </button>
                ) : (
                    <button
                        className="button"
                        onClick={() => handleFollowButtonClick(writerName, !isFollowing)}
                        disabled={isLoading}
                    >
                        {isLoading ? (
                            <span className="loading-spinner"></span>
                        ) : isMine ? (
                            '수정하기'
                        ) : isFollowing ? (
                            '친구끊기'
                        ) : (
                            '친구맺기'
                        )}
                    </button>
                )}
            </div>
        </div>
    );
};

export default FollowButtonByName;