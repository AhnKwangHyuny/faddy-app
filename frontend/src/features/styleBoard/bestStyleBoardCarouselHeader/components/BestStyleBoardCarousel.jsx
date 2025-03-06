import React, { useState } from "react";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

const BestStyleBoardCarousel = () => {
  const [slides, setSlides] = useState([
    { id: 1, content: "트렌디한 스타일링, StyleBoard로 완성하기", icon: "star", likes: 120 },
    { id: 2, content: "StyleBoard로 만나는 감각적인 홈 데코", icon: "help_outline", likes: 85 },
    { id: 3, content: "StyleBoard: 나만의 공간을 스타일리시하게", icon: "event_note", likes: 98 },
    { id: 4, content: "StyleBoard로 꾸미는 개성 넘치는 인테리어", icon: "style", likes: 150 },
    { id: 5, content: "StyleBoard로 완성하는 모던 & 심플 홈스타일", icon: "notifications", likes: 110 }
  ]);

  const settings = {
    dots: false,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 3000,
    fade: true,
    cssEase: "linear"
  };

  return (
    <div className="carousel-container">
      <Slider {...settings}>
        {slides.map(slide => (
          <div key={slide.id} className="carousel-slide">
            <div className="carousel-content">
              <span className="material-icons carousel-icon">{slide.icon}</span>
              <span className = "content">{slide.content}</span>
              <div className="like-section">
                <span className="material-icons like-icon">thumb_up</span>
                <span className="like-count">{slide.likes}</span>
              </div>
            </div>
          </div>
        ))}
      </Slider>
    </div>
  );
};

export default BestStyleBoardCarousel;
