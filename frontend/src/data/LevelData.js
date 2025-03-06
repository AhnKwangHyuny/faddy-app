// 레벨 데이터 객체
const levelData = {
    LEVEL_1: { level: "Lv1", color: "#070A17", name: "일반인" },
    LEVEL_2: { level: "Lv2", color: "#EDD710", name: "옷좀입는사람" },
    LEVEL_3: { level: "Lv3", color: "#47BD35", name: "패션센스굿" },
    LEVEL_4: { level: "Lv4", color: "#4B6BFB", name: "패션트랜더" },
    LEVEL_5: { level: "Lv5", color: "#FB4B80", name: "인플루언서" },
};

// 레벨에 따른 객체 반환 함수
export const getLevelInfo = (level) => {
    return levelData[level] || null; // 해당 레벨이 없을 경우 null 반환
};
