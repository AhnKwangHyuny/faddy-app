import {getLevelInfo} from "data/LevelData";
import {formatCreatedAt} from "pages/styleBoardDetailPage/utils/formatDate";
import {mapCategoryToTopic} from "pages/styleBoardCreatePage/data/mapTopicToCategory";

export const mapStyleBoardToView = (itemList) => {
  return itemList.map(item => ({
    id: item.boardId,
    title: item.title,

    category: mapCategoryToTopic(item.category),

    createdAt : formatCreatedAt(item.createdAt),

    userProfile : {
        level : getLevelInfo(item.userProfileDTO.level).level,
        nickname : item.userProfileDTO.nickname,
        profileImageUrl : item.userProfileDTO.profileImageUrl,
    },

    interaction : {
      likes : item.interactionCountDTO.likeCount,
      views : item.interactionCountDTO.viewCount,
      comments : item.interactionCountDTO.commentCount
    }
  }));
};

