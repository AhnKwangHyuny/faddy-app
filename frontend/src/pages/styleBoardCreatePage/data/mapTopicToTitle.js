import {topics} from "./topicsData";
import {isValidTopic} from "./validTopics";

export const mapTopicToTitle = (topic) => {
    return topics[topic] || '알 수 없는 주제';
};