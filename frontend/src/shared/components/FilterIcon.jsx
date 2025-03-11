import React from 'react';

const FilterIcon = ({ setShowModal }) => {
  const handleClick = () => {
    setShowModal(true);
  };

  return (
    <div onClick={handleClick} className="filter-icon">
      <span className="material-symbols-outlined">
        filter_list
      </span>
    </div>
  );
};

export default FilterIcon;
