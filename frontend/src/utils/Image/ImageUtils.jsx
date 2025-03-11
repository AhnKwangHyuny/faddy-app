import {saveImage} from 'api/post';
import {deleteImages , deleteImage} from "api/delete";

export const UploadImage = async (event , category) => {

    // 파일 하나만 가져오기
    const file = event.target.files[0];

    if (!file) {
        alert('파일이 업로드되지 않았습니다.');
        return;
    }

    if(!category) {
        alert('이미지 카테고리가 선택되지 않았습니다.');
        return;
    }

    const maxSize = 50 * 1024 * 1024;
    if (file.size > maxSize) {
        alert('파일 크기가 5MB를 초과합니다.');

        return;
    }
    const formData = new FormData();

    formData.append('image', file);

    try {
        // image 정보 반환 (이름 , url , hashName , size ... )
        return await saveImage(formData , category);
    } catch (error) {
        alert('파일 업로드에 실패했습니다. 다시 시도해 주세요.');
        return;
    }

}

export const uploadImageFromFile = async (file , category) => {

    if (!file) {
        alert('파일이 업로드되지 않았습니다.');
        return;
    }

    if(!category) {
        alert('이미지 카테고리가 선택되지 않았습니다.');
        return;
    }

    const maxSize = 50 * 1024 * 1024;
    if (file.size > maxSize) {
        alert('파일 크기가 5MB를 초과합니다.');

        return;
    }
    const formData = new FormData();

    formData.append('image', file);

    try {
        // image 정보 반환 (이름 , url , hashName , size ... )
        return await saveImage(formData , category);
    } catch (error) {
        alert('파일 업로드에 실패했습니다. 다시 시도해 주세요.');
        return;
    }

}


const convert_image_to_dto = (imageObj) => {
        if(!imageObj.url || !imageObj.hashedName) {
            // 이미지 실패 로직
            return alert ("이미지 삭제에 실패했습니다. ");

        }

        const dto = {
            hashedName : imageObj.hashedName,
            url : imageObj.url,
        }

        return dto;
}

const convert_imageList_to_dto = (imageList) => {

    const dto = [];

    //imageList를 순회하면서 imageList의 요소인 hashedName 과 url를 찾음
    imageList.forEach((image) => {

        // 각 이미지에서 hashedName과 url을 추출
        const {url , hashedName} = image;

        const imageInfo = {
            "hashedName" : hashedName,
            "url" : url,
        }

        dto.push(imageInfo);

    });


    return dto;
}

export const deleteImageList = async (imageList) => {

    if(imageList.length === 0) {
        return;
    }

    const requestBody = convert_imageList_to_dto(imageList);

    try {
        // image 정보 반환 (이름 , url , hashName , size ... )
        return await deleteImages(requestBody);

    } catch (error) {
        console.error(error);
        alert('이미지 삭제에 실패했습니다.');
        return;
    }
};


// 이미지 삭제 함수
export const delImage = async (image) => {
    // 이미지가 없으면 반환
    if (!image) {
        console.warn('삭제할 이미지가 없습니다.');
        return;
    }

    // 이미지를 DTO로 변환
    const requestBody = convert_imageList_to_dto(image);

    // 변환된 DTO에 필수 요소가 없으면 반환
    if (!requestBody) {
        console.warn('이미지 변환에 실패했습니다.');
        return;
    }

    // 이미지 삭제 API 호출
    try {
        const result = await deleteImage(requestBody);
        console.log('이미지 삭제 성공:', result);
        return result;
    } catch (error) {
        console.error('이미지 삭제 중 오류 발생:', error);
        alert('이미지 삭제에 실패했습니다.');
    }
};

