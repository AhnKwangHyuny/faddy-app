import React, { useEffect, useState } from "react";
import ProfileFollowAction from "./Components/ProfileFollowAction/ProfileFollowAction";
import Header from "pages/SnapDetail/Components/Header/header";
import { ImageCarouselWithHTML } from "widgets/Carousel/Carousel";
import Interaction from "features/interaction";
import HashtagViewer from "widgets/HashtagViewer/HashtagViewer";
import TextArea from "widgets/TextArea/TextArea";
import CommentInputBox from "widgets/Comment/CommentInputBox/CommentInputBox";
import { useNavigate } from "react-router-dom";
import { getSnapData } from "api/get";
import SnapIdContext from "shared/context/SnapIdContext";


const SnapDetail = () => {
    const navigate = useNavigate();
    const encryptedSnapId = new URLSearchParams(window.location.search).get('snapId');
    const [snap, setSnap] = useState(null);
    const [userId, setUserId] = useState(null); // userId 상태 추가
    const [snapData, setSnapData] = useState(null);

    // 컴포넌트 마운트 시 userId 확인 및 snap data 서버에 요청
    useEffect(() => {
        // 스냅 아이디가 존재하지 않으면 전 페이지로 리다이렉트
        if (!encryptedSnapId) {
            alert("접근할 수 없는 스냅입니다.");
            return navigate(-1);
        }

        const loadData = async () => {
            try {
                const response = await getSnapData(encryptedSnapId);
                const data = response.data.data;
                console.log(data);
                // snap data 유효성 검증 (추후 개발)
                if (!data) {
                    navigate(-1); // 전 페이지로 이동
                }
                setSnapData(data);

            } catch (error) {
                console.error("데이터를 불러오는 데 실패했습니다.", error);
                alert("데이터를 불러오는데 실패했습니다.");
                navigate(-1); // 전 페이지로 이동
            }
        };
        loadData();

    }, [encryptedSnapId, userId]);

    const testSetting = {
        dots: true,
        infinite: true,
        speed: 600,
        slidesToShow: 1,
        slidesToScroll: 1,
        arrows: false,
    };


    return (
        <SnapIdContext.Provider value={encryptedSnapId}>
            <div>
                {snapData && (
                    <>
                        <Header />
                        <div className="main__body">
                            <ProfileFollowAction user={snapData.user} />
                            <ImageCarouselWithHTML
                                setting={testSetting}
                                imageListHtml={
                                    snapData.images.map((image, index) => (
                                            <img key={index} src={image.imageUrl} alt={image.hashName || ''} />
                                    ))
                                }
                            />
                        </div>
                        <Interaction objectId = {encryptedSnapId} />
                        <HashtagViewer hashtagList={snapData.hashTags} />
                        <TextArea textData={snapData.description} />
                        <CommentInputBox />
                    </>
                )}
            </div>
        </SnapIdContext.Provider>

    );
};

export default SnapDetail;