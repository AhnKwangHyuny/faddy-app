import React, { useState, useEffect } from 'react';
import Header from 'features/UserSelectorModal/components/Header';
import UserSelectorList from 'features/UserSelectorModal/components/UserSelectorList';
import SearchBar from 'features/SearchBar/SearchBar';
import { getFollowerListByFollowingId } from 'api/get';
import { createFriendChatRoom } from 'api/post';
import { useNavigate } from 'react-router-dom';

const UserSelectorModal = ({ isShowModal, onClose }) => {

   const [isClosed, setIsClosed] = useState(!isShowModal);
   const [followedUsers, setFollowedUsers] = useState([]);
   const [isActive, setIsActive] = useState(false);
   const navigate = useNavigate();

   // 선택된 사용자 ID 목록 상태 관리
   const [selectedUserIds, setSelectedUserIds] = useState([]);

   const handleClose = () => {
       setIsClosed(true);
       onClose();
   };

   // 대화상대 변경
   const handleUserSelectionChange = (selectedUserIds) => {
       setSelectedUserIds(selectedUserIds);
       setIsActive(selectedUserIds.length > 0);
   };

   // 사용자가 대화상대를 선택 또는 선택 해제할 때
   const handleUserSelect = (userId) => {
       setSelectedUserIds((prevSelectedUserIds) => {
           const newSelectedUserIds = prevSelectedUserIds.includes(userId)
               ? prevSelectedUserIds.filter((id) => id !== userId)
               : [...prevSelectedUserIds, userId];
           handleUserSelectionChange(newSelectedUserIds);
           return newSelectedUserIds;
       });
       console.log(selectedUserIds);
   };

   // 새로운 채팅방을 생성하는 함수
   const handleCreateChatRoom = async () => {
       console.log("선택된 유저 ID:", selectedUserIds);
       // 채팅 생성 요청 API 호출
       const request = {
           masterId: localStorage.getItem('userId'),
           memberIds: selectedUserIds,
           type: "FRIEND"
       };
       // 서버에 친쿠톡 데이터 리스트 요청 API
       const result = await createFriendChatRoom(request);
       console.log(result);
       // 결과 상태가 '200'이면 채팅방으로 이동
       if(result.status === 200){
            navigate(`/talks/detail/friend/${result.data.roomId}`);
            return;
       };
   }

   // 팔로우된 사용자 목록을 가져오는 함수
   useEffect(() => {
       const fetchFollowedUsers = async () => {
           try {
               const users = await getFollowerListByFollowingId();
               setFollowedUsers(users);
           } catch (error) {
               console.error('팔로우 리스트를 가져오는데 실패했습니다.', error);
           }
       };

       fetchFollowedUsers();
   }, []);

   return (
       <div className="user-selector-modal">
           <div className="modal-main-container">
               <Header onClose={handleClose} onDone={handleCreateChatRoom} isActive={isActive} />
               <SearchBar />
               <div className="friend-container">
                   <span className="friend"> 친구 </span>
               </div>
               <UserSelectorList
                   followedUsers={followedUsers}
                   onUserSelectionChange={handleUserSelectionChange}
                   handleUserSelect={handleUserSelect}
                   selectedUserIds={selectedUserIds}
               />
           </div>
       </div>
   );
};

export default UserSelectorModal;
