
import React from 'react';
import Header from 'widgets/Header/Header';
import BottomNavigationBar from 'widgets/BottomNavigationBar/BottomNavigationBar';


const Layout = ({ children }) => (
  <>
    <Header />
    {children}
    <BottomNavigationBar/>
  </>
);

export default Layout;