import React from 'react';
import BestStyleBoardCarousel from 'features/styleBoard/bestStyleBoardCarouselHeader/components/BestStyleBoardCarousel';
import FilterIcon from "shared/components/FilterIcon";
import 'features/styleBoard/bestStyleBoardCarouselHeader/styles/_bestStyleBoardCarouselHeader.scss';

const BestStyleBoardCarouselHeader = ({setShowModal}) => {
  return (
    <div className = "bestStyleBoardCarouselHeader-container">
        <BestStyleBoardCarousel />
        <FilterIcon setShowModal = {setShowModal} />
    </div>

  );
}

export default BestStyleBoardCarouselHeader;