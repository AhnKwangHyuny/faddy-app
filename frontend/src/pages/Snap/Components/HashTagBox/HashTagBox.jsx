import React, { useRef, useEffect, useState } from 'react';
import Tagify from '@yaireo/tagify';
import '@yaireo/tagify/dist/tagify.css';

const HashTagBox = ({ labelHide = false, tags, setTags }) => {
    const inputRef = useRef(null);
    const tagifyInstance = useRef(null);

    useEffect(() => {
        if (inputRef.current && !tagifyInstance.current) {
            tagifyInstance.current = new Tagify(inputRef.current, {
                maxTags: 5,
                placeholder: "type something",
                dropdown: {
                    enabled: 0,
                    maxItems: 5,
                },
            });

            tagifyInstance.current.on('change', handleTagChange);
            tagifyInstance.current.on('remove', handleTagRemove);

            // Set initial tags if provided
            if (tags && tags.length > 0) {
                tagifyInstance.current.addTags(tags);
            }
        }

        return () => {
            if (tagifyInstance.current) {
                tagifyInstance.current.off('change', handleTagChange);
                tagifyInstance.current.off('remove', handleTagRemove);
                tagifyInstance.current.destroy();
                tagifyInstance.current = null;
            }
        };
    }, []);

    useEffect(() => {
        // Update Tagify tags only if there is a difference
        if (tagifyInstance.current) {
            const currentTags = tagifyInstance.current.value.map(tag => tag.value);
            if (JSON.stringify(currentTags) !== JSON.stringify(tags)) {
                tagifyInstance.current.loadOriginalValues(tags);
            }
        }
    }, [tags]);

    const handleTagChange = (e) => {
        if (e.detail.value === "") {
            return;
        }

        const values = JSON.parse(e.detail.value);
        const tagList = values.map(item => item.value);
        setTags(tagList);
    };

    const handleTagRemove = (e) => {
        const { index } = e.detail;

        if (tags.length === 0) {
            return;
        }

        if (index === tags.length - 1) {
            setTags(tags.slice(0, -1));
        } else {
            setTags(tags.filter((_item, i) => i !== index));
        }
    };

    useEffect(() => {
        console.log('Tags updated:', tags);
    }, [tags]);

    return (
        <div className="tags">
            <div className="tags__wrapper">
                <div className="tags__header">
                    {!labelHide && <div className="label">태그</div>}
                </div>
                <div className="tags__input">
                    <input
                        ref={inputRef}
                        name="tags-outside"
                        className="tagify--outside"
                        placeholder="#원하는 태그를 입력 후 엔터를 눌러주세요 (최대 5개)"
                    />
                </div>
                <div className="tags__footer">
                    <div className="line"></div>
                </div>
            </div>
        </div>
    );
};

export default HashTagBox;
