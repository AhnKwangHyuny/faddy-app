export const formatCount = (number) => {
    if (number >= 10000) {
        // 소수점 첫째 자리가 0인지 확인하여 포맷팅
        const divided = number/10000;
        const formatted = divided % 1 === 0 ? Math.floor(divided) : divided.toFixed(1);
        return `${formatted}만`;
    } else if (number >= 1000) {
        return number.toLocaleString();
    } else {
        return number.toString();
    }
}

