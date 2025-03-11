export const isValidURL = (string) => {
    try {
        new URL(string);
        return true;
    } catch (e) {
        return false;
    }
}

export const isImageURL = (url) => {
    // 이미지 파일 확장자를 확인하는 패턴
    const imagePattern = /\.(png|jpg|jpeg|gif|bmp|webp)$/i;

    // S3 버킷 URL과 이미지 경로를 확인하는 패턴
    const s3ImagePattern = /https:\/\/.*\.s3\.amazonaws\.com\/.*\/images\//i;

    return imagePattern.test(url) || s3ImagePattern.test(url);
};
