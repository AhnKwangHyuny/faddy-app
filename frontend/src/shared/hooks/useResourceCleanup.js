import {useCallback, useEffect} from 'react';
import {useLocation} from 'react-router-dom';
import usePrevious from "shared/hooks/url/usePrevious";


/**
 *  url 변경 및 리다이렉트 시 초기화할 resource 서버에 삭제 요청하는 커스텀 훅
 * */
const useResourceCleanup = (resourceToClean, deleteRequestCallback) => {
    const location = useLocation();
    const prevLocation = usePrevious(location);

    const handleRedirect = useCallback(async () => {
        if (resourceToClean && deleteRequestCallback) {
            console.log('Cleaning up resource:', resourceToClean);
            await deleteRequestCallback(resourceToClean);
        }
    }, [resourceToClean, deleteRequestCallback]);

    useEffect(() => {
        // 새로고침 또는 URL 변경 시 실행
        if (location.pathname !== prevLocation?.pathname) {
            handleRedirect();
        }
    }, [location, prevLocation, handleRedirect]);
};

export default useResourceCleanup;
