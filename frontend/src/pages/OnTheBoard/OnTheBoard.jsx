import * as React from 'react';
import { Link } from 'react-router-dom';
import on_the_board from 'shared/img/on_the_board.png';

const ImageSlider = () => (
  <div className="image-slider">
    <img loading="lazy" src={on_the_board} className="image-slider__img" />
    <img loading="lazy" src={on_the_board} className="image-slider__img" />
  </div>
);

const StyledButton = ({ children, className }) => (
  <div className={className}>{children}</div>
);

function OnTheBoard(props) {
  return (
    <div className="on-the-board">
      <div className="on-the-board__container">
        <ImageSlider />
        <div className="on-the-board__circle">Faddy</div>
        <div className="on-the-board__content">
          <div className="on-the-board__text">
            자신의 패션을 자유롭게
            <br />
            공유 하세요.
          </div>
          <StyledButton className="on-the-board__button-main">
            <Link to="/styleShare">메인으로</Link>
          </StyledButton>
          <StyledButton className="on-the-board__button-login">
            <Link to="/login">로그인</Link>
          </StyledButton>
          <div className="on-the-board__terms">
            By continuing, you agree to the{" "}
            <span>Terms of Service</span> and confirm
            <br />
            that you have read our{" "}
            <span>Privacy Policy</span>.
          </div>
        </div>
      </div>
    </div>
  );
}

export default OnTheBoard;
