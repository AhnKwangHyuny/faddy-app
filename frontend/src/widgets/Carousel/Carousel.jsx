import React, {useCallback, useState} from "react";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import HighlightSnapCard from "widgets/HighlightSnapCard/HighlightSnapCard";

function ImageCarouselWithComponents({ settings, componentList }) {
    const customSettings = customSettings(settings)
    return (
        <section className="carousel">
            <Slider {...customSettings} className="mp-0">
                {componentList.map((Component, index) => (
                    <div className="image" key={`component-${index}`}>
                        <div className="image__wrapper">
                            <Component />
                        </div>
                    </div>
                ))}
            </Slider>
        </section>
    );
}

function ImageCarouselWithHTML({ settings, imageListHtml =[] }) {
    const defaultSettings = {
        dots: true,
        infinite: true,
        speed: 600,
        slidesToShow: 1,
        slidesToScroll: 1,
        arrows: false,
        ...settings,
    };
    console.log(imageListHtml);

    return (
        <section className="carousel">
            <Slider {...defaultSettings} className="mp-0">
                {imageListHtml.length > 0 ? (
                    imageListHtml.map((item, index) => (
                        <div className="image" key={index}>
                            <div className="image__wrapper">{item}</div>
                        </div>
                    ))
                ) : (
                    <div className="image">
                        <div className="image__wrapper">
                            <p>이미지가 없습니다.</p>
                        </div>
                    </div>
                )}
            </Slider>
        </section>
    );
}

function ImageCarouselWithSnapCards({ settings, snapDataList }) {
    const [dragging, setDragging] = useState(false);

    const handleBeforeChange = useCallback(() => {
        setDragging(true);
    }, [setDragging]);

    const handleAfterChange = useCallback(() => {
        setDragging(false);
    }, [setDragging]);



    const defaultSettings = {
        dots: true,
        infinite: true,
        speed: 600,
        slidesToShow: 1,
        slidesToScroll: 1,
        arrows: false,
        ...settings,
    };

    return (
        <section className="carousel">
            <Slider beforeChange={handleBeforeChange}
                    afterChange={handleAfterChange}
                    {...defaultSettings}
                    className="mp-0"
            >
                {snapDataList.map(({ username, imageUrl, ranking }, index) => (
                    <div className="image" key={`component-${index}`}>
                        <div className="image__wrapper">
                            <HighlightSnapCard username={username} imageUrl={imageUrl} ranking={ranking} />
                        </div>
                    </div>
                ))}
            </Slider>
        </section>
    );
}

export { ImageCarouselWithComponents, ImageCarouselWithHTML ,  ImageCarouselWithSnapCards};