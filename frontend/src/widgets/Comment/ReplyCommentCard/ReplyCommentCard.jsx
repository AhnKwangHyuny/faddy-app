const ReplyComment = ({ reply }) => {
    return (
        <div className="reply-comment-card">
            <div className="reply-comment-header">
                <span>{reply.author}</span>
                <span>{reply.createdAt}</span>
            </div>
            <div className="reply-comment-body">{reply.content}</div>
        </div>
    );
};