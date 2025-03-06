import { snapCategoryData} from 'data/category';
import {checkUserIdExists} from "../../api/get";
export const isValidDescription = (description) => {
    // 설명 필드 유효성 검사 로직 구현
    return description.length <= 1500;
};

export const isValidTags = (tags) => {
    // 태그 유효성 검사 로직 구현
    return tags.length <= 10 && tags.every(tag => typeof tag === 'string' && tag.trim().length > 0);
};

export const isValidCategories = (selectedCategories) => {

    // 데이터 타입 객체인지 , 비어 있지 않은지 확인
    if (typeof selectedCategories !== 'object' || Object.keys(selectedCategories).length === 0) {
        return false;
    }

    // object 카테고리 데이터를 배열로 전환 키 : 값의 형태로 만들어
    // snap_category에서 categoryGroup와 일치하는 카테고리를 찾고 없다면 false 반환
    return Object.entries(selectedCategories).every(([categoryGroup, selectedItem]) => {
        const matchingCategory = snapCategoryData.find(c => c.label === categoryGroup);

        // matchingCategory가 undefined인 경우 false 반환
        if (!matchingCategory) {
            return false;
        }
        return matchingCategory.subCategories.includes(selectedItem);
    });
};

export const isValidUserId = async (userId) => {
    // 사용자 ID 유효성 검사 로직 구현
    if (!(typeof userId === 'string' && userId !== "")) {
        return false;
    }

    try {
        // db에 사용자 있는지 확인
        const response = await checkUserIdExists(userId);

        return response.status === 200;
    } catch (error) {
        return false;
    }
};