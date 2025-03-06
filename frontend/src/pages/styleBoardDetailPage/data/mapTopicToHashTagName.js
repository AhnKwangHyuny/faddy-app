import { topics } from "pages/styleBoardCreatePage/data/topicsData";

const topicToHashTagNameMap = {
    question: '질문',
    daily: '일상공유',
    fashion: '패션공유',
    tips: '스타일 팁',
    news: '소식'
};

export const mapTopicToHashTagName = (topic) => {
    return topicToHashTagNameMap[topic] || "알 수 없음";
};
