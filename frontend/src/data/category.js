// export const snap_category = [
//     {
//         group: "성별",
//         items: ["남자" , "여자"]
//     },
//     {
//         group: "나이",
//         items: ["10대" , "20대" , "30대", "40대" , "50+"],
//     },
//     {
//         group:"계절",
//         items: ["봄" ,"여름","가을","겨울"]
//     },
//     {
//         group:"스타일",
//         items: ["캐주얼" ,"아메카지" , "댄디", "올드머니", "리조트", "스트릿" , "기타"]
//     }
// ];

export const snapCategoryData = [
    {
        id: 'gender',
        label: '성별',
        subCategories: ['남자', '여자'],
    },
    {
        id: 'age',
        label: '나이',
        subCategories: ['10대', '20대', '30대', '40대', '50+'],
    },
    {
        id: 'season',
        label: '계절',
        subCategories: ['봄', '여름', '가을', '겨울'],
    },
    {
        id: 'style',
        label: '스타일',
        subCategories: ['캐주얼', '아메카지', '댄디', '올드머니', '리조트', '스트릿', '기타'],
    },

];

export const searchFilterData = [
    {
        id: 'sorting',
        label: '정렬',
        subCategories: ['최신순', '인기순' , '조회순'],
    },

    {
        id: 'style',
        label: '스타일',
        subCategories: ['질문하기', '일상공유', '패션공유', '패션꿀팁'],

    }
]