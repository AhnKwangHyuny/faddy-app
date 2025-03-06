import React from 'react';
import Spinner from "shared/animation/Spinner@1.gif";
const LoadingAnimation = () => {
    return (
        <div className="loading-animation">
            <img src={Spinner} className="spinner" alt="Spinner@1"/>
        </div>
    );
};

export default LoadingAnimation;