import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

import OnTheBoard from "pages/OnTheBoard/OnTheBoard";
import LoginPage from "pages/Login/LoginPage";
import Signup from "pages/SignUp/Signup";
import StyleShare from "pages/StyleShare/StyleShare";
import SnapCreation from "pages/Snap/Index";
import { useHideAddressBar } from 'shared/hooks/useHideAddressBar';
import ProtectedRoute from "routes/ProtectedRoute";
import StyleTalk from "pages/StyleTalk/index.jsx";

// style Board
import StyleBoardListPage from "pages/styleBoardListPage/index";
import StyleBoardCreatePage from "pages/styleBoardCreatePage/index";
import StyleBoardDetailPage from "pages/styleBoardDetailPage/index";

import {AuthProvider} from 'shared/context/AuthContext';
import SnapDetail from "pages/SnapDetail/index";
import StyleTalkRoom from "pages/StyleTalkRoom/index";
import StyleBoardEditPage from "pages/styleBoardEditPage/index";
import Login from "pages/Login/test";

function App() {
    useHideAddressBar();

    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/" element={<OnTheBoard />} />
                    <Route path="/styleShare" element={<StyleShare />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/signup/*" element={<Signup />} />
                    <Route path="/snaps" element={<ProtectedRoute><SnapCreation /></ProtectedRoute>} />
                    <Route path="/snaps/detail" element={<SnapDetail/>} />
                    <Route path="/talks" element={<ProtectedRoute><StyleTalk/></ProtectedRoute>}/>
                    <Route path="/talks/detail/:type/:id" element={<ProtectedRoute><StyleTalkRoom/></ProtectedRoute>}/>
                    <Route path="/styleBoards" element={<StyleBoardListPage/>} />
                    <Route path="/styleBoards/create" element={<ProtectedRoute><StyleBoardCreatePage/></ProtectedRoute>} />
                    <Route path="/styleBoards/detail/:id" element={<StyleBoardDetailPage/>} />
                    <Route path="/styleBoards/edit/:id" element={<StyleBoardEditPage/>} />
                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;
