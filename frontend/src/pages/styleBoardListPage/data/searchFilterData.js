export const searchFilterData = {
    '정렬': {
        '최신순': 'newest',
        '인기순': 'popular',
        '조회순': 'view',
    },
    '스타일': {
        '질문하기': 'question',
        '일상공유': 'daily',
        '패션공유': 'fashion',
        '패션꿀팁': 'tips',
    }
};

export const transformFilterOptions = (filterOptions) => {

    const transformedOptions = {};
    console.log(filterOptions.category);
    console.log(filterOptions.category['정렬']);
    console.log(filterOptions.category['스타일']);

    if (filterOptions.category) {
        const { 정렬, 스타일 } = filterOptions.category;
        transformedOptions['sort'] = searchFilterData['정렬'][정렬] || 정렬;
        transformedOptions['category'] = searchFilterData['스타일'][스타일] || 스타일;
    }

    if (filterOptions.tags) {
        transformedOptions['tags'] = filterOptions.tags;
    }

    return transformedOptions;
};
