import taggingData from "data/taggingData";
import {ImageCarouselWithHTML} from 'widgets/Carousel/Carousel';
import {taggingCarouselSettings} from 'widgets/Carousel/carouselSetting'

const TaggingImages = () => {

    const testData = taggingData;

    const taggingList = [];

    const convertTaggingDataToJSX = (taggingData , index) => {

        return (
            <div className="tagging-wrapper" data-index={`td-${index}`}>
                <div className="tagging">
                    <div className="tagging-image__wrapper">
                        <img
                            src={taggingData.imageUrl}
                            alt={taggingData.name}
                            className="tagging-image"
                        />
                    </div>
                    <div className="tagging-name">
                          <span className="name">
                              #{taggingData.name}
                          </span>
                    </div>

                </div>
            </div>
        )
    };

    testData.map((data, index) => {

        // data 값들이 존재하는 경우에만 렌더링
        if (data && data.imageUrl && data.name) {
            taggingList.push(convertTaggingDataToJSX(data , index));
        }
    });


    return (
        <div className="tagging-list">
            <div className="tagging-list__wrapper">
                <ImageCarouselWithHTML settings={taggingCarouselSettings} imageListHtml = {taggingList}/>
            </div>
        </div>
    );
}

export default TaggingImages;