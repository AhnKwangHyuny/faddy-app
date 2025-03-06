import React, { useEffect, useState } from 'react';
import Thumbnail from "widgets/Snap/Thumbnail/Thumbnail";
import groupThumbnailsByPair from "utils/thumbnail/groupThumbnailsByPair";
import { useInView } from "react-intersection-observer";
import { getMoreThumbnails } from "api/get";
import LoadingAnimation from "shared/ui/LoadingUI/Loading"

const loadThumbnailsFromServer = async (page, setThumbnails, setHasMore) => {
   try {
       const data = await getMoreThumbnails(page);
       setThumbnails(prevThumbnails => [...prevThumbnails, ...data]);
       setHasMore(data.length === 4); // 추가 데이터가 더 있는지 확인
   } catch (error) {
       console.error(`Error loading thumbnails: ${error.message}`);
   }
};

const ThumbnailList = () => {
   const [thumbnails, setThumbnails] = useState([]);
   const [page, setPage] = useState(-1);
   const [hasMore, setHasMore] = useState(true);
   const [ref, inView] = useInView();

   useEffect(() => {
       if (inView && hasMore) {
           loadThumbnailsFromServer(page, setThumbnails, setHasMore);
           setPage(prevPage => prevPage + 1);
       }
   }, [inView]);

   useEffect(() => {
       const handleScroll = () => {
           // 스크롤 위치 관련 로직이 필요한 경우 여기에 작성
       };

       window.addEventListener('scroll', handleScroll);
       return () => {
           window.removeEventListener('scroll', handleScroll);
       };
   }, []);

   const groups = groupThumbnailsByPair(thumbnails);

   return (
       <ul className="thumbnail-list">
           {groups.length === 0 ? (
               <div className="no-groups-message">
                   등록된 스냅이 존재하지 않습니다.
               </div>
           ) : (
               groups.map((group, index) => (
                   <li className="thumbnail-group" key={index}>
                       {group.map((thumbnail, i) => (
                           <div key={i} className="thumbnail-wrapper">
                               {thumbnail && (
                                   <Thumbnail data={thumbnail} />
                               )}
                           </div>
                       ))}
                   </li>
               ))
           )}
           <div ref={ref}>
               <LoadingAnimation />
           </div>
       </ul>
   );
}

export default ThumbnailList;