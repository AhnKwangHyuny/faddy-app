const menuData = {
 '/styleShare': [
   { title: "마이 스냅", src: "/snaps", type: 'link' },
   { title: "홈", src: "/styleShare", type: 'link' },
   { title: "포스팅", src: "/styleBoards", type: 'link' },
   { title: "패션 톡", src: "/talks", type: 'link' },
   { title: "팔로워", src: "/", type: 'link' },
 ],
 '/talks': [
   { title: "검색", src: "/", type: 'link' },
   { title: "지금 뜨는", src: "/", type: 'link' },
   { title: "내 채팅방", src: "/", type: 'link' },
   { title: "#새 채팅방", type: 'modal' , modalType: 'CreateRoom' },
 ],
 '/styleBoards' : [
    { title: "모든주제", src: "/styleBoards/detail/1?topic=question", type: 'link' },
    { title: "#질문", src: "/styleBoards/create", type: 'link' },
    { title: "#일상", src: "/styleBoards/create", type: 'link' },
    { title: "#패션", src: "/styleBoards/create", type: 'link' },
    { title: "#소식", src: "/styleBoards/create", type: 'link' },
     ]
}

export default menuData;