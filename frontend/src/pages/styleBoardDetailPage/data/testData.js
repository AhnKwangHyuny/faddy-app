export const testData = {
    id: 1,
    topic: "질문",
    title: "여기서 모자 색을 뭐로 하면 좋을까?",
    content: `
        <h3>아 난 여기 모자를 어떤색으로 해야할 지 모르겠어.</h3>
        <p>이것은 <strong>테스트</strong> 본문입니다. <em>기울임꼴</em>과 <u>밑줄</u> 텍스트도 포함됩니다.</p>
        <hr>
        <ul>
            <li>첫 번째 목록 항목</li>
            <li>두 번째 목록 항목</li>
            <li>세 번째 목록 항목</li>
        </ul>
        <p>다음은 이미지입니다:</p>
    `,
    tags: ["테스트", "게시글", "ReactQuill"],
    userProfile: {
        nickname: "테스터아이디",
        level: "Lv.4 패션트랜더",
        profileImageUrl: "/default_profile.jpg",
        createdAt: "18시간 전"
    },
    interaction: {
        views: 1234,
        likes: 56
    }
};

//
//export const testComments = [
//    {
//      id: 1,
//      author: {
//        level: "Lv4",
//        profileImageUrl: "/default_profile.jpg",
//        username: '사용자1',
//      },
//      content: '멋진 사진이네요! 배경도 정말 예뻐요.',
//      createdAt: '19시간 전',
//      like: {
//        count: 3,
//        isLike: false,
//      },
//      replyComments: [
//        {
//          id: 101,
//          author: {
//            level: "Lv2",
//            profileImageUrl: "/default_profile.jpg",
//            username: '사용자2',
//          },
//          content: '감사합니다! 저도 이 사진이 마음에 듭니다.',
//          createdAt: '15시간 전',
//          like: {
//            count: 1,
//            isLike: true,
//          },
//        },
//        {
//          id: 102,
//          author: {
//            level: "Lv3",
//            profileImageUrl: "/default_profile.jpg",
//            username: '사용자3',
//          },
//          content: '저도 이 사진 정말 좋아요. 다음에 또 올려주세요!',
//          createdAt: '12시간 전',
//          like: {
//            count: 2,
//            isLike: false,
//          },
//        },
//      ],
//    },
//    {
//      id: 2,
//      author: {
//        level: "Lv3",
//        profileImageUrl: "/default_profile.jpg",
//        username: '사용자2',
//      },
//      content: '옷도 잘 어울려요!',
//      createdAt: '20시간 전',
//      like: {
//        count: 0,
//        isLike: false,
//      },
//    },
//    {
//      id: 3,
//      author: {
//        level: "Lv2",
//        profileImageUrl: "/default_profile.jpg",
//        username: '사용자3',
//      },
//      content: '저도 이런 사진 찍어보고 싶어요!',
//      createdAt: '21시간 전',
//      like: {
//        count: 1,
//        isLike: false,
//      },
//    },
//];
//

