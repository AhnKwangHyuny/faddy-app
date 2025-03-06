import {topics} from "./topicsData";

export const isValidTopic = (topic) => {
    return Object.keys(topics).includes(topic);
};
