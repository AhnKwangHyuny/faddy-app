// levelUtils.js
import { level_data } from "entities/user/model";

const getLevelData = (level) => {
    return level_data.find((data) => data.level === level) || {};
};

export default getLevelData;