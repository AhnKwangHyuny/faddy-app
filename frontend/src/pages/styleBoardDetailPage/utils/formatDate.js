export const formatCreatedAt = (createdAt) => {

    if (!Array.isArray(createdAt) || createdAt.length < 6) {
        throw new Error('Invalid createdAt array');
    }

    // Extract the first 6 values from createdAt array
    const [year, month, day, hour, minute, second] = createdAt;

    // Convert to UTC Date object
    const date = new Date(Date.UTC(year, month - 1, day, hour, minute, second)); // Month is 0-based in JS Date

    const now = new Date();
    const diff = now.getTime() - date.getTime(); // Get difference in milliseconds

    const seconds = Math.floor(diff / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);
    const months = Math.floor(days / 30);
    const years = Math.floor(days / 365);

    if (years > 0) {
        return `${years}년 전`;
    } else if (months > 0) {
        return `${months}달 전`;
    } else if (days > 0) {
        return `${days}일 전`;
    } else if (hours > 0) {
        return `${hours}시간 전`;
    } else if (minutes > 0) {
        return `${minutes}분 전`;
    } else {
        return `방금 전`;
    }
};
