import React, { useState, useRef, useEffect, useMemo } from 'react';
import { uploadImageFromFile } from "utils/Image/ImageUtils";
import { deleteImage , deleteImages } from "api/delete";
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';

const ContentEditor = ({ content, setContent }) => {
    const quillRef = useRef(null);
    const IMAGE_CATEGORY = 'style_board';
    const [imageList, setImageList] = useState([]);
    const imageListRef = useRef(imageList);
    const [deletedImages, setDeletedImages] = useState([]);

    useEffect(() => {
        imageListRef.current = imageList;
    }, [imageList]);

    useEffect(() => {
        if (quillRef.current) {
            const quillInstance = quillRef.current.getEditor();
            const toolbar = quillInstance.getModule('toolbar');

            // 이미지 업로드 핸들러 등록
            toolbar.addHandler('image', handleImageUpload);

            // 텍스트 변경 이벤트 핸들러 등록 (이미지 삭제 감지용)
            quillInstance.on('text-change', handleTextChange);

            // Cleanup 함수: 컴포넌트가 unmount될 때 이벤트 리스너 제거
            return () => {
                quillInstance.off('text-change', handleTextChange);
            };
        }
    }, []);

    // 페이지 새로고침, 리다이렉트 또는 페이지 뒤로 가기 버튼 클릭 시 이미지 삭제
    useEffect(() => {
        const handleBeforeUnload = async (event) => {
            try {
                await deleteImages(imageListRef.current);
                console.log('Images deleted successfully on unload');
            } catch (error) {
                console.error('Error deleting image on unload:', error);
            }
        };

        window.addEventListener('beforeunload', handleBeforeUnload);

        return () => {
            window.removeEventListener('beforeunload', handleBeforeUnload);
        };
    }, []);

    // 이미지 url를 통해 useState imageList에 저장된 imageInfo 조회
    const findImageInfoByUrl = (imageUrl) => {
        return imageListRef.current.find(imageInfo => imageInfo.url === imageUrl);
    };

    // 이미지 업로드 핸들러
    const handleImageUpload = () => {
        const input = document.createElement('input');
        input.setAttribute('type', 'file');
        input.setAttribute('accept', 'image/*');
        input.click();

        input.onchange = async () => {
            const file = input.files[0];

            // 이미지 업로드 api 요청
            const uploadedImage = await uploadImageFromFile(file, IMAGE_CATEGORY);

            console.log(uploadedImage.data);

            const imageInfo = {
                hashedName: uploadedImage.data.hashedName,
                url: uploadedImage.data.url,
            };

            setImageList(prevList => {
                const updatedList = [...prevList, imageInfo];
                imageListRef.current = updatedList; // 업데이트된 리스트를 ref에도 반영
                return updatedList;
            });

            // 이미지 저장 후 url 반환 (s3 object url)
            if (uploadedImage?.data?.url != null) {
                const quillInstance = quillRef.current.getEditor();
                const range = quillInstance.getSelection(true);

                // 이미지를 삽입하는 부분을 업데이트
                quillInstance.insertEmbed(range.index, 'image', uploadedImage.data.url);
                quillInstance.setSelection(range.index + 1); // 이미지 삽입 후 커서를 이미지 다음으로 이동
                return;
            }

            // 이미지 전송 오류 시
            alert('이미지 업로드에 실패했습니다.');
            return;
        };
    };

    // 텍스트 변경 핸들러
    const handleTextChange = (delta, oldDelta, source) => {
        if (source === 'user') {
            const quillInstance = quillRef.current.getEditor();
            const currentContents = quillInstance.getContents();

            // 현재 에디터에 있는 이미지 URL을 수집
            const currentImages = [];
            currentContents.ops.forEach(op => {
                if (op.insert && op.insert.image) {
                    currentImages.push(op.insert.image);
                }
            });

            // 삭제된 이미지를 찾고, 이미 삭제 요청이 나간 이미지를 제외
            const newDeletedImages = imageListRef.current.filter(imageInfo =>
                !currentImages.includes(imageInfo.url) && !deletedImages.includes(imageInfo.url)
            );

            // 삭제된 이미지 요청 보내기
            newDeletedImages.forEach(imageInfo => {
                sendDeleteRequest(imageInfo.url);
            });

            // 삭제된 이미지 URL을 상태에 추가
            setDeletedImages(prevList => [...prevList, ...newDeletedImages.map(image => image.url)]);
        }
    };

    // 서버로 이미지 삭제 요청 전송 핸들러
    const sendDeleteRequest = async (imageUrl) => {
        try {
            // 이미지 url을 통해 imageInfo 조회
            const imageInfo = findImageInfoByUrl(imageUrl);
            if (imageInfo) {
                console.log(imageUrl, imageListRef.current);
                console.log(imageInfo);
                await deleteImage(imageInfo);
                console.log('Image deleted successfully');

                // 이미지 리스트 업데이트
                setImageList(prevList => prevList.filter(image => image.url !== imageUrl));
            }
        } catch (error) {
            console.error('Error deleting image:', error);
        }
    };

    // 최초 렌더링 시 modules 저장 후 매 렌더링 마다 module 새로 생성하는 이슈 해결
    const modules = useMemo(() => {
        return {
            toolbar: {
                container: [
                    ['image'],
                    [{ header: [1, 2, 3, false] }],
                    ['bold', 'italic', 'underline', 'strike', 'blockquote'],
                ],
                handlers: {
                    image: handleImageUpload,
                },
            },
        };
    }, []);

    return (
        <div className="content-editor-container">
            <ReactQuill
                ref={quillRef}
                value={content}
                onChange={setContent}
                placeholder="본문을 입력하세요"
                className="content-editor"
                modules={modules}
            />
        </div>
    );
};

export default ContentEditor;
