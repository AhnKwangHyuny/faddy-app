import Layout from "widgets/Layout/Layout";
import ChatTypeSelector from "pages/StyleTalk/components/ChatTypeSelector/ChatTypeSelector";
import ChatList from "pages/StyleTalk/components/ChatList/ChatList";
import CreateRoomModal from "pages/StyleTalk/components/CreateRoomModal/CreateRoomModal";
import UserSelectorModal from "features/UserSelectorModal/UserSelectorModal";
import { useState } from "react";
import {getTalkList} from "api/get";


const StyleTalk = () => {

    const [isCreateRoomModalOpen, setIsCreateRoomModalOpen] = useState(false);
    const [selectedChatType, setSelectedChatType] = useState("FRIEND");

    // 채팅 타입에 따른 chatList 데이터 서버에 api요청
    const fetchChatList = async (chatType) => {
        try {
            const chatList = await getTalkList(chatType);
            console.log(chatList);
        } catch (error) {
            console.error('채팅 리스트를 가져오는데 실패했습니다.', error);
        }
    };

    // chatType 변경 핸들러
     const handleChatTypeChange = (type) => {
            setSelectedChatType(type);
        };

    return (
       <Layout>
           <ChatTypeSelector onChatTypeChange={handleChatTypeChange} />
           <ChatList chatType = {selectedChatType} fetchChatList={fetchChatList}/>
       </Layout>
    );

};

export default StyleTalk;