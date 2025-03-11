import React from 'react';

const TagList = ({ topic, tags }) => {
    return (
        <div className="tag-list">
            <span className="tag topic">{topic}</span>
            {tags.map((tag, index) => (
                <span key={index} className="tag">#{tag}</span>
            ))}
        </div>
    );
};

export default TagList;
