import React from "react";
import StyleBoard from "./StyleBoard";
import LoadingAnimation from "shared/ui/LoadingUI/Loading";

const StyleBoardList = ({ styleBoards = [], listRef }) => {
    return (
        <div>
            <ul>
                {styleBoards.map((styleBoard) => (
                    <li key={styleBoard.id}>
                        <StyleBoard data={styleBoard} />
                    </li>
                ))}
            </ul>

            <div ref={listRef}>
                <LoadingAnimation />
            </div>
        </div>
    );
}

export default StyleBoardList;
