import axios from 'axios';
import {isValidCategories, isValidDescription, isValidTags, isValidUserId} from 'utils/snap/SnapDataValidation';
import {userRequestInstance} from "api/axiosInstance";
import {END_POINTS} from "constants/api";
import {uploadSnap} from "../../api/post";
import {useNavigate} from "react-router-dom";

const UploadSnap = async (userId, imageList, description, tags, selectedCategories) => {

    try {
        /*** 입력 데이터 유효성 검사 */
        const isValidUser = await isValidUserId(userId);

        if (!isValidUser) {
            throw new Error('Invalid user ID');
        }
        if(imageList.length === 0) {
            throw new Error("이미지를 한 장 이상 꼭 넣어주세요!");
        }
        if (!isValidDescription(description)) {
            // descripton으로 커서 이동
            alert("description 입력은 필수입니다!");
            return;
        }
        if (!isValidTags(tags)) {
            // 태그로 커서 이동
            alert("해시태그 입력은 필수입니다!");
            return;
        }


        if (!isValidCategories(selectedCategories)) {
            //카테고리고 커서 이동
            alert("카테고리 선택은 필수입니다!");
            return;
        }

        // 태그 엔티티 생성 및 ID 목록 받아오기
        const hashTagRequestData = {
            "contentType" : "SNAP",
            tags : tags
        }

        const hashTagIds = await createTags(hashTagRequestData);
        console.log(hashTagIds);
        // 카테고리 엔티티 생성 및 ID 목록 받아오기
        const categoryPairs = {
            contentType : "SNAP",
            "categories" : Object.keys(selectedCategories).map(key => ({
                [key] : selectedCategories[key]
            }))
        }
        const categoryIds = await createCategories(categoryPairs);
        // requestBody에 담을 데이터 서버 측 dto에 맞게 가공

        imageList = imageList.map(({format, originalName, size, ...rest}) => rest);

        // Snap 생성 requestBody data set
        const snapData = { description, userId , hashTagIds, categoryIds, imageList };

        // snap 생성 요청 api
        const response = await uploadSnap(snapData);

        const snapId = response.data.snapToken;

        // 이미지 엔티티와 Snap 엔티티 간 관계 지어주기
        await updateSnapImageRelationship(imageList, snapId);

        // 성공 메시지 등 필요한 처리
        return { success: true , snapId};

    } catch (error) {

        console.error('Error uploading Snap post:', error);
        return { success: false,  error: error.message };
    }
};

export const updateSnapImageRelationship = async (imageList, snapId) => {
    try {
        const requestBody = { "snapId": snapId, "images": imageList };
        // await userRequestInstance.put(, requestBody);
    } catch (error) {
        console.error('Error linking images to Snap:', error);
        throw error;
    }
};

export const createTags = async (requestData) => {
    try {
        const response = await userRequestInstance.post(END_POINTS.UPLOAD_HASH_TAG, requestData);

        return response.data;

    } catch (error) {
        console.error('Error creating tags:', error);
        throw error;
    }
};

export const createCategories = async (categoryPairs) => {
    try {
        // 카테고리 엔티티 생성 및 ID 목록 받아오기
        const response = await userRequestInstance.post(END_POINTS.UPLOAD_CATEGORY, categoryPairs);

        return response.data;
    } catch (error) {
        console.error('Error creating categories:', error);
        throw error;
    }
};


export default UploadSnap;


