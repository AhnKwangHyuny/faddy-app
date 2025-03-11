// usePrevious.js
import { useEffect, useRef } from "react";

function usePrevious(value) {
    const ref = useRef();

    useEffect(() => {
        if (value !== undefined) {
            ref.current = value;
        }
    }, [value]);

    return ref.current;
}

export default usePrevious;