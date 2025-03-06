import {formatCreatedAt} from './formatDate';

export const mapToComment = (comment) => {
   console.log(comment);
   return {
       id: comment.id,
       content: comment.content,
       created_at: formatCreatedAt(comment.created_at),
       author: {
           nickname: comment.author.nickname,
           level: comment.author.level,
           profileImageUrl: comment.author.profileImageUrl,
       },
       likeCount : comment.likeCount,
       isLiked : comment.liked,
       content: comment.content,
       parentId: comment.parentId || null,
       replies: comment.replies || null,
   };
};