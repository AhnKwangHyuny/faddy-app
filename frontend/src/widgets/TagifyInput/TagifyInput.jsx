import React, { useRef, useEffect } from 'react';
import Tagify from '@yaireo/tagify';
import '@yaireo/tagify/dist/tagify.css';
import styled from 'styled-components';

const TagifyInputWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
`;

const TagifyLabel = styled.label`
  font-size: 14px;
  font-weight: 600;
  color: #333;
`;

const TagifyInputContainer = styled.div`
  position: relative;
  .tagify__input {
    padding: 12px 16px;
    font-size: 16px;
    border: 1px solid #ccc;
    border-radius: 4px;
    resize: none;
    outline: none;
    width: 100%;
    &:focus {
      border-color: #007bff;
    }
  }
  .tagify {
    border: none;
    padding: 0;
    margin-top: 8px;
  }
`;

const TagifyInput = ({ label, value, onChange, placeholder }) => {
    const tagifyRef = useRef(null);
    const inputRef = useRef(null);

    useEffect(() => {
        if (!tagifyRef.current) {
            tagifyRef.current = new Tagify(inputRef.current, {
                maxTags: 5,
                originalInputValueFormat: (valuesArr) =>
                    valuesArr.map((item) => item.value).join(','),
                callbacks: {
                    change: (e) => {

                        const newTags = tagifyRef.current.value;
                        onChange(newTags);
                    },
                },

                placeholder: true,

                dropdown: {
                    position: "input",
                    enabled : 0
                }
            });
        }

        return () => {
            if (tagifyRef.current) {
                tagifyRef.current.destroy();
                tagifyRef.current = null;
            }
        };
    }, [onChange]);

    return (
        <TagifyInputWrapper>
            <TagifyInputContainer>
                <input
                    ref={inputRef}
                    value={value}
                    placeholder={placeholder}
                    className="tagify__input"
                />
                <div className="tagify"></div>
            </TagifyInputContainer>
            <TagifyLabel>{label}</TagifyLabel>
        </TagifyInputWrapper>
    );
};

export default TagifyInput;
