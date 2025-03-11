import CreationMenuBar from "widgets/CreationMenuBar/CreationMenuBar";
import ImageUploader from "widgets/ImageUploader/ImageUploader";
import Description from "pages/Snap/Components/Description/Description";
import HashTagBox from "./Components/HashTagBox/HashTagBox";
import CategorySelector from "pages/Snap/Components/SnapCategory/SnapCategory";
import {useState} from "react";
import {useAuth} from "shared/context/AuthContext";
import uploadSnap from "features/snap/UploadSnap";
import {useNavigate} from "react-router-dom";
import useResourceCleanup from "shared/hooks/useResourceCleanup";
import {deleteImageList} from "utils/Image/ImageUtils";
import {END_POINTS} from "../../constants/api";

const Snap = () => {
    const [imageList, setImageList] = useState([]);
    const [description, setDescription] = useState("");
    const [tags, setTags] = useState([]);
    const [selectedCategories, setSelectedCategories] = useState({});
    const { userId } = useAuth();

    const navigate = new useNavigate();
    const handleUploadSnap = async () => {
        try {
            const result = await uploadSnap(userId.userId, imageList, description, tags, selectedCategories);

            if (result.success && result.snapId) {
                // snap 포스팅 성공 시 해당 스냅 detail page로 이동
                navigate(`${END_POINTS.SNAP_DETAIL_LOCATION}?snapId=${result.snapId}`);
            } else {
                console.error(result.error);
                alert("스냅 포스팅을 실패했습니다. 다시 작성해주세요.");

                // 현재 스냅 작성하는 페이지로 다시 리다이렉트
                window.location.reload();
            }
        } catch (error) {
            console.error("Error uploading SNS post:", error);

            alert("알 수 없는 에러가 발생했습니다.");

            // 현재 스냅 작성하는 페이지로 다시 리다이렉트
            window.location.reload();
        }
    };

    //s3 image 저장 후 리로딩 시 (리다이렉트) image삭제 요청
    useResourceCleanup(imageList , deleteImageList);

    return (
        <section className="main__body">
            <CreationMenuBar
                imageList={imageList}
                description={description}
                tags={tags}
                selectedCategories={selectedCategories}
                onUploadSnap={handleUploadSnap}
            />
            <ImageUploader imageList={imageList} setImageList={setImageList} />
            <Description description={description} setDescription={setDescription} />
            <HashTagBox tags={tags} setTags={setTags} />
            <CategorySelector selectedCategories={selectedCategories} setSelectedCategories={setSelectedCategories} />
        </section>
    );
};

export default Snap;