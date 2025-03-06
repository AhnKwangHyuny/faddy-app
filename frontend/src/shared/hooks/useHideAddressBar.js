import { useEffect } from 'react';

export const useHideAddressBar = () => {

    useEffect(() => {
        const browser = window;
        const doc = browser.document;

        if (!window.location.hash || !browser.addEventListener) {
            window.scrollTo(0, 1);
            let scrollTop = 1;

            const checkWindowBody = setInterval(() => {
                if (doc.body) {
                    clearInterval(checkWindowBody);
                    scrollTop = "scrollTop" in doc.body ? doc.body.scrollTop : 1;
                    browser.scrollTo(0, scrollTop === 1 ? 0 : 1);
                }
            }, 15);

            if (browser.addEventListener) {
                browser.addEventListener("load", () => {
                    setTimeout(() => {
                        browser.scrollTo(0, scrollTop === 1 ? 0 : 1);
                    }, 0);
                }, false);
            }
        }
    }, []);
};
