import Layout from 'widgets/Layout/Layout';
import TrendSnapCardList from "pages/StyleShare/components/TrendSnapList/TrendSnapList";
import React from "react";
import SnapSortingButton from "pages/StyleShare/components/SnapSortingButton/SnapSortingButton";
import SortingMenu from "features/Sorting/SortingMenu/SortingMenu";
import ThumbnailList from "features/thumbnail/ThumbnailList/ThumbnailList";
import TaggingImages from "widgets/TaggingImage/TaggingImages";

function StyleShare() {

    return (
        <Layout>
            <section id="style-share">
                <TrendSnapCardList/>
                <SnapSortingButton/>
                <SortingMenu/>
                <TaggingImages/>

                <section className="thumbnail-container">
                    <div className="thumbnail-list-wrapper">
                        <div className="thumbnail">
                            <ThumbnailList/>
                        </div>
                    </div>
                </section>

            </section>
        </Layout>
    );

};


export default StyleShare;
