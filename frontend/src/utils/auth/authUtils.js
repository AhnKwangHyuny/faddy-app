export const initiateLocalStorage = (...keys) => {
    keys.forEach(key => {
        // 로컬 스토리지에 해당 키가 있으면 그 키의 값을 초기화합니다.
        if(localStorage.getItem(key) !== null) {
            localStorage.removeItem(key);
        }});
    };
