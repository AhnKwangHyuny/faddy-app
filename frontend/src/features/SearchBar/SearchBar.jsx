import React, { useState, useEffect } from 'react';

const SearchBar = ({ placeholder = "닉네임, 아이디로 검색", searchFunction }) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);

  useEffect(() => {
    if (searchQuery !== '') {
      searchFunction(searchQuery).then((results) => {
        setSearchResults(results);
      });
    }
  }, [searchQuery, searchFunction]);

  const handleSearch = (e) => {
    setSearchQuery(e.target.value);
  };

  return (
    <div className="searchBar">
      <input
        type="text"
        placeholder={placeholder}
//         value={searchQuery}
//         onChange={handleSearch}
      />
      <ul>
        {searchResults.map((result) => (
          <li key={result.id}>{result.name}</li>
        ))}
      </ul>
    </div>
  );
};

export default SearchBar;