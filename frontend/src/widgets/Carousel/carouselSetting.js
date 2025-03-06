export const taggingCarouselSettings = {
    dots: false,
    infinite: true,
    speed: 600,
    slidesToShow: 5,
    slidesToScroll: 1,
    arrows: false,
    autoplay : true,
    autoplaySpeed: 2500,
};


export const trendingSnapCarouselSettings = {
    dots: false,
    infinite: true,
    speed: 700,
    slidesToShow: 2,
    slidesToScroll: 1,
    arrows: false,
    draggable: true, // enable drag and drop
    // swipeToSlide: true, // enable swipe gesture
    initialSlide: 1,
    pauseOnHover : true,
};


export const defaultSettings = {
        dots: true,
        infinite: true,
        speed: 600,
        slidesToShow: 1,
        slidesToScroll: 1,
        arrows: false,
};

export const customSettings = (settings) => {
    return {
        dots: true,
        infinite: true,
        speed: 600,
        slidesToShow: 1,
        slidesToScroll: 1,
        arrows: false,
        ...settings
    }
}