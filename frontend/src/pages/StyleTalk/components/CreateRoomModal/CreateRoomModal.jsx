import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import UserSelectorModal from 'features/UserSelectorModal/UserSelectorModal';

const CreateRoomModal = ({ isShowModal, onClose }) => {
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(isShowModal);
  const [isUserSelectorOpen, setIsUserSelectorOpen] = useState(false);

  useEffect(() => {
    setIsModalOpen(isShowModal);
  }, [isShowModal]);

  const handleOpenChatClick = () => {
    navigate('/talk/rooms/new?type=open');
    setIsModalOpen(false);
    onClose();
  };

  const handleFriendChatClick = () => {
    setIsUserSelectorOpen(true);
    setIsModalOpen(false);
  };

  const handleUserSelectorClose = () => {
    setIsUserSelectorOpen(false);
    onClose();
  };

  const handleModalClose = () => {
    setIsModalOpen(false);
    onClose();
  };

  useEffect(() => {
    if (!isShowModal) {
      setIsUserSelectorOpen(false);
    }
  }, [isShowModal]);

  return (
    <div className="ChatTypeSelector">
      {isModalOpen && (
        <div className="modal">
          <div className="modal-header">
            <button onClick={handleModalClose} className="close-modal">
              <span className="material-icons">close</span>
            </button>
            <div className="title-wrapper">
              <span className="title">새로운 채팅</span>
            </div>
          </div>
          <div className="chatTypeSelector__container">
            <div className="chat-name__container">
              <div className="chat-name">
                <div className="chat-name__wrapper">
                  <button className="chat-name__button" onClick={handleOpenChatClick}>
                    <span className="material-icons-outlined icon">add_comment</span>
                    <span className="sorting-name">오픈톡</span>
                  </button>
                </div>
              </div>
              <div className="chat-name">
                <div className="chat-name__wrapper">
                  <button className="chat-name__button" onClick={handleFriendChatClick}>
                    <span className="material-icons-outlined icon">group_add</span>
                    <span className="sorting-name">친구톡</span>
                  </button>
                </div>
              </div>
              <div className="chat-name">
                <div className="chat-name__wrapper">
                  <button className="chat-name__button" onClick={handleFriendChatClick}>
                    <span className="material-icons-outlined icon">lock</span>
                    <span className="sorting-name">개인톡</span>
                  </button>
                </div>
              </div>
            </div>
            <hr className="hdlne line" />
          </div>
        </div>
      )}
      {isUserSelectorOpen && (
        <UserSelectorModal
          isShowModal={isUserSelectorOpen}
          onClose={handleUserSelectorClose}
        />
      )}
    </div>
  );
};

export default CreateRoomModal;
