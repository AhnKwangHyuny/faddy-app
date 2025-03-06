import React, { useState, useEffect } from 'react';
import Layout from "widgets/Layout/Layout";
import { useInView } from "react-intersection-observer";
import BestStyleBoardCarouselHeader from 'features/styleBoard/bestStyleBoardCarouselHeader/index';
import SearchFilterModal from "features/Search/components/SearchFilterModal";
import StyleBoardList from "pages/styleBoardListPage/components/StyleBoardList";
import CreateTopicModal from "pages/styleBoardListPage/components/CreateTopicModal";
import FloatingActionButton from 'shared/components/FloatingActionButton';
import { useLocation } from 'react-router-dom';
import { getStyleBoards } from "pages/styleBoardListPage/api/get";
import { mapStyleBoardToView } from "pages/styleBoardListPage/data/mapStyleBoardData";
import { transformFilterOptions } from "pages/styleBoardListPage/data/searchFilterData";

const StyleBoardListPage = () => {
    const [showSearchFilterModal, setShowSearchFilterModal] = useState(false);
    const [showCreateTopicModal, setShowCreateTopicModal] = useState(false);
    const [showFloatingButton, setShowFloatingButton] = useState(true);
    const location = useLocation();

    const [styleBoards, setStyleBoards] = useState([]);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const { ref, inView } = useInView();
    const [filterOptions, setFilterOptions] = useState({});

    const loadStyleBoardData = async (filters) => {
        try {
            if (filters && Object.keys(filters).length > 0) {
                filters = transformFilterOptions(filters);
            }

            // filters와 filterOptions가 동일한 경우 setFilterOptions를 호출하지 않음
            if (JSON.stringify(filterOptions) !== JSON.stringify(filters)) {
                setFilterOptions(filters);
                setPage(0);
                setStyleBoards([]);
            }

            const data = await getStyleBoards({ ...filters, page, size: 4 });
            const viewers = mapStyleBoardToView(data);

            setStyleBoards(prevStyleBoards => (page === 0 ? viewers : [...prevStyleBoards, ...viewers]));
            setHasMore(data.length === 4);
        } catch (e) {
            console.error(`Error loading styleBoard data: ${e.message}`);
        }
    };

    useEffect(() => {
        if (inView && hasMore) {
            loadStyleBoardData(filterOptions);
        }
    }, [inView, filterOptions, page]);

    useEffect(() => {
        setShowFloatingButton(true);
    }, [location]);

    const handleCreateTopicClick = () => {
        setShowCreateTopicModal(true);
        setShowFloatingButton(false);
    };

    const handleCloseCreateTopicModal = () => {
        setShowCreateTopicModal(false);
        setShowFloatingButton(true);
    };

    return (
        <Layout>
            <section style={{ padding: '20px 10px' }}>
                <BestStyleBoardCarouselHeader setShowModal={setShowSearchFilterModal} />
                <StyleBoardList styleBoards={styleBoards} listRef={ref} />
                <SearchFilterModal
                    showModal={showSearchFilterModal}
                    setShowModal={setShowSearchFilterModal}
                    setFilterOptions={setFilterOptions}
                    onSubmit={(filters) => {
                        setFilterOptions(filters);
                        setPage(0);
                    }}
                />
                <CreateTopicModal showModal={showCreateTopicModal} setShowModal={handleCloseCreateTopicModal} />
                {showFloatingButton && <FloatingActionButton onClick={handleCreateTopicClick} />}
            </section>
        </Layout>
    );
};

export default StyleBoardListPage;
