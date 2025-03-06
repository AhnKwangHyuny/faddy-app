import { topics } from './topicsData';

const topicToCategoryMap = {
    question: 'QUESTION',
    daily: 'DAILY_SHARE',
    fashion: 'FASHION_SHARE',
    tips: 'STYLE_TIP',

};

export const mapTopicToCategory = (topic) => {
    return topicToCategoryMap[topic] || null;
};

export const mapCategoryToTopic = (category) => {
    return Object.keys(topicToCategoryMap).find(key => topicToCategoryMap[key] === category) || null;
};
