import React from 'react';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import 'react-quill/dist/quill.bubble.css';

import {testData} from 'pages/styleBoardDetailPage/data/testData';

const ContentViewer = ({ title , content }) => {
    console.log(content);
    return (
        <div className="content-viewer">
            <div className = "title-container">
                <span className = "title"> {title}</span>
            </div>

            <div className="content" dangerouslySetInnerHTML={{ __html: content }} />


        </div>
    );
};

export default ContentViewer;
