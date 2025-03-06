const DATA_LOAD_ERROR_MESSAGE = "[error] 데이터를 불러오는데 실패했습니다.";

function validateData(data) {
    const errors = [];

    // title 필드 검증
    if (!data.title || typeof data.title !== 'string' || data.title.trim() === '') {
        errors.push('title');
    }

    // content 필드 검증
    if (!data.content || typeof data.content !== 'string' || data.content.trim() === '') {
        errors.push('content');
    }

    // category 필드 검증
    if (!data.category || typeof data.category !== 'string' || data.category.trim() === '') {
        errors.push('category');
    }

    // nickname 필드 검증
    if (!data.nickname || typeof data.nickname !== 'string' || data.nickname.trim() === '') {
        errors.push('nickname');
    }

    // profileImageUrl 필드 검증
    if (!data.profileImageUrl || typeof data.profileImageUrl !== 'string' || data.profileImageUrl.trim() === '') {
        errors.push('profileImageUrl');
    }

    // userLevel 필드 검증
    if (!data.userLevel || typeof data.userLevel !== 'string' || data.userLevel.trim() === '') {
        errors.push('userLevel');
    }
    console.log(errors);
    // 필드 중 하나라도 누락된 경우 에러 처리
    if (errors.length > 0) {
        throw new Error(DATA_LOAD_ERROR_MESSAGE);
    }
}


